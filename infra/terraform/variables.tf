#############################
# 기본
#############################
variable "region" {
  description = "AWS 리전"
  type        = string
  default     = "ap-northeast-2"
}

variable "prefix" {
  description = "리소스 이름 접두사"
  type        = string
  default     = "terra"
}

variable "instance_name" {
  description = "EC2 Name 태그"
  type        = string
  default     = ""
}

variable "additional_tags" {
  description = "공통 태그 추가"
  type        = map(string)
  default     = {}
}

variable "timezone" {
  description = "컨테이너 TZ"
  type        = string
  default     = "Asia/Seoul"
}

variable "instance_type" {
  description = "EC2 인스턴스 타입"
  type        = string
  default     = "t3.micro"
}

variable "root_volume_size" {
  description = "EC2 루트 EBS 사이즈(GB)"
  type        = number
  default     = 30
}

#############################
# 보안 / 노출 토글
#############################
variable "open_npm_admin" {
  description = "NPM(81) 외부 개방 여부"
  type        = bool
  default     = true
}

variable "enable_ssh" {
  description = "SSH(22) 임시 허용 여부"
  type        = bool
  default     = false
}

variable "ssh_cidr" {
  description = "SSH 허용 CIDR (enable_ssh=true일 때만 사용)"
  type        = string
  default     = "127.0.0.1/32"
}

variable "expose_mysql" {
  description = "MySQL(3306) 외부 공개 여부"
  type        = bool
  default     = false
}

variable "db_client_cidr" {
  description = "MySQL(3306) 허용 CIDR (expose_mysql=true일 때만 사용)"
  type        = string
  default     = "127.0.0.1/32"
}

#############################
# 앱 / DB
#############################
variable "default_password" {
  description = "초기/공통 비밀번호 (NPM/Redis/MySQL)"
  type        = string
  sensitive   = true
}

variable "app_db_name" {
  description = "애플리케이션 DB 이름"
  type        = string
  default     = "appdb"
}

variable "mysql_user_list" {
  description = "MySQL 사용자 목록"
  type = list(object({
    name       = string
    host       = string
    password   = string
    privileges = string
  }))
  default = []
}

variable "npm_admin_email" {
  description = "NPM 관리자 이메일"
  type        = string
  default     = "admin@npm.local"
}

#############################
# 레지스트리(GHCR) / 이미지 / SSH 키
#############################
variable "github_user" {
  description = "GitHub 사용자/오너 (GHCR 로그인 필요 시)"
  type        = string
  default     = ""
}

variable "github_token" {
  description = "GitHub 토큰(GHCR)"
  type        = string
  sensitive   = true
  default     = ""
}

variable "npm_image" {
  description = "NPM 이미지"
  type        = string
  default     = "jc21/nginx-proxy-manager:latest"
}

variable "redis_image" {
  description = "Redis 이미지"
  type        = string
  default     = "redis:latest"
}

variable "mysql_image" {
  description = "MySQL 이미지"
  type        = string
  default     = "mysql:latest"
}

variable "elasticsearch_image" {
  description = "Elasticsearch 이미지"
  type        = string
  default     = "docker.elastic.co/elasticsearch/elasticsearch:8.18.5"
}

variable "public_key_path" {
  description = "SSH 공개키 경로 (enable_ssh=true일 때만 사용)"
  type        = string
  default     = "~/.ssh/id_rsa.pub"
}

#############################
# GitHub OIDC 연동용 변수들
#############################
variable "github_owner" {
  description = "GitHub Organization or User"
  type        = string
}

variable "github_repo" {
  description = "GitHub Repository name"
  type        = string
}

variable "github_ref" {
  description = "허용 브랜치 refs. 기본은 main만"
  type        = string
  default     = "refs/heads/main"
}

variable "gha_role_name" {
  description = "GitHub Actions용 IAM Role 이름 prefix"
  type        = string
  default     = "gha-deploy"
}