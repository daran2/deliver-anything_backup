#!/bin/bash
set -euxo pipefail

# --- Swap (4GB) ---
if ! grep -q "/swapfile" /etc/fstab; then
  dd if=/dev/zero of=/swapfile bs=128M count=32
  chmod 600 /swapfile
  mkswap /swapfile
  swapon /swapfile
  echo "/swapfile swap swap defaults 0 0" >> /etc/fstab
fi

# --- Docker 설치 ---
dnf update -y
dnf install -y docker git
systemctl enable --now docker
usermod -aG docker ec2-user || true

# --- Docker 네트워크 생성 ---
docker network create common || true

# --- 디렉터리 생성 및 권한 설정 ---
# Elasticsearch
mkdir -p /dockerProjects/elasticsearch/volumes/data
chown -R 1000:1000 /dockerProjects/elasticsearch/volumes/data

# --- GHCR 로그인 systemd 등록 (자동 재로그인용) ---
if [ -n "${github_token}" ] && [ -n "${github_user}" ]; then
  cat >/etc/systemd/system/ghcr-login.service <<'EOF'
[Unit]
Description=GHCR Docker Login
After=network-online.target docker.service
Wants=network-online.target

[Service]
Type=oneshot
User=root
EnvironmentFile=/etc/ghcr-login.env
ExecStart=/bin/bash -c 'echo "$GITHUB_TOKEN" | docker login ghcr.io -u "$GITHUB_USER" --password-stdin || true'

[Install]
WantedBy=multi-user.target
EOF

  cat >/etc/ghcr-login.env <<EOF
GITHUB_USER=${github_user}
GITHUB_TOKEN=${github_token}
EOF

  chmod 600 /etc/ghcr-login.env
  systemctl daemon-reload
  systemctl enable ghcr-login.service
  systemctl start ghcr-login.service
fi

# --- Nginx Proxy Manager ---
docker rm -f npm || true
docker run -d \
  --name npm \
  --restart unless-stopped \
  --network common \
  -p 80:80 \
  -p 443:443 \
%{ if open_npm_admin ~}
  -p 81:81 \
%{ else ~}
  -p 127.0.0.1:81:81 \
%{ endif ~}
  -e TZ=${timezone} \
  -e INITIAL_ADMIN_EMAIL=${npm_admin_email} \
  -e INITIAL_ADMIN_PASSWORD=${default_password} \
  -v /dockerProjects/npm/volumes/data:/data \
  -v /dockerProjects/npm/volumes/etc/letsencrypt:/etc/letsencrypt \
  ${npm_image}

# --- Elasticsearch ---
docker rm -f elasticsearch || true
docker run -d \
  --name elasticsearch \
  --restart unless-stopped \
  --network common \
  -p 9200:9200 \
  -p 9300:9300 \
  -e "discovery.type=single-node" \
  -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
  -e "xpack.security.enabled=false" \
  -v /dockerProjects/elasticsearch/volumes/data:/usr/share/elasticsearch/data \
  -u 1000:1000 \
  ${elasticsearch_image}

# --- Redis ---
docker run -d \
  --name redis \
  --restart unless-stopped \
  --network common \
  -p 6379:6379 \
  -e TZ=${timezone} \
  -v /dockerProjects/redis/volumes/data:/data \
  ${redis_image} \
  redis-server --appendonly yes --requirepass ${default_password}

# --- MySQL ---
docker run -d \
  --name mysql \
  --restart unless-stopped \
  --network common \
%{ if expose_mysql ~}
  -p 3306:3306 \
%{ endif ~}
  -e MYSQL_ROOT_PASSWORD=${default_password} \
  -v /dockerProjects/mysql/volumes/var/lib/mysql:/var/lib/mysql \
  -v /dockerProjects/mysql/volumes/etc/mysql/conf.d:/etc/mysql/conf.d \
  ${mysql_image}

# --- MySQL 준비 대기 ---
until docker exec mysql mysql -uroot -p${default_password} -e "SELECT 1" &> /dev/null; do
  sleep 5
done

# --- 앱 DB 생성 ---
docker exec mysql mysql -uroot -p${default_password} -e "CREATE DATABASE IF NOT EXISTS \`${app_db_name}\`;"

# --- 사용자/권한 ---
%{ for u in mysql_user_list ~}
docker exec mysql mysql -uroot -p${default_password} -e "CREATE USER IF NOT EXISTS '${u.name}'@'${u.host}' IDENTIFIED WITH caching_sha2_password BY '${u.password}';"
docker exec mysql mysql -uroot -p${default_password} -e "GRANT ${u.privileges} TO '${u.name}'@'${u.host}';"
%{ endfor ~}
docker exec mysql mysql -uroot -p${default_password} -e "FLUSH PRIVILEGES;"