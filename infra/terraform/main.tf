terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

########################
# Locals
########################
locals {
  ec2_name_tag = "${var.prefix}-ec2"

  # 공통 태그
  common_tags = var.additional_tags

  name_prefix = var.prefix

  # user_data 템플릿 렌더링
  ec2_user_data = templatefile("${path.module}/user_data.tpl", {
    open_npm_admin   = var.open_npm_admin
    timezone         = var.timezone
    npm_admin_email  = var.npm_admin_email
    default_password = var.default_password
    expose_mysql     = var.expose_mysql
    app_db_name      = var.app_db_name
    mysql_user_list  = var.mysql_user_list
    github_user      = var.github_user
    github_token     = var.github_token
    npm_image        = var.npm_image
    redis_image      = var.redis_image
    mysql_image      = var.mysql_image
    elasticsearch_image = var.elasticsearch_image
  })
}

provider "aws" {
  region = var.region
}

data "aws_availability_zones" "available" {
  state = "available"
}

########################
# Networking
########################
resource "aws_vpc" "main" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = merge(local.common_tags, { Name = "${var.prefix}-vpc" })
}

resource "aws_subnet" "public" {
  count                   = 2
  vpc_id                  = aws_vpc.main.id
  cidr_block              = cidrsubnet(aws_vpc.main.cidr_block, 4, count.index)
  availability_zone       = data.aws_availability_zones.available.names[count.index]
  map_public_ip_on_launch = true

  tags = merge(local.common_tags, { Name = "${var.prefix}-subnet-public-${count.index}" })
}

resource "aws_subnet" "private" {
  count                   = 2
  vpc_id                  = aws_vpc.main.id
  cidr_block              = cidrsubnet(aws_vpc.main.cidr_block, 4, count.index + 2)
  availability_zone       = data.aws_availability_zones.available.names[count.index]
  map_public_ip_on_launch = false

  tags = merge(local.common_tags, { Name = "${var.prefix}-subnet-private-${count.index}" })
}

resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.main.id
  tags   = merge(local.common_tags, { Name = "${var.prefix}-igw" })
}

resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = merge(local.common_tags, { Name = "${var.prefix}-rt-public" })
}

resource "aws_route_table_association" "public_assoc" {
  count          = length(aws_subnet.public)
  subnet_id      = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public.id
}

########################
# Security Group
########################
resource "aws_security_group" "ec2_sg" {
  name   = "${var.prefix}-sg"
  vpc_id = aws_vpc.main.id

  ingress { 
    from_port  = 80
    to_port    = 80
    protocol   = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port  = 443
    to_port    = 443 
    protocol   = "tcp" 
    cidr_blocks = ["0.0.0.0/0"] 
  }

  dynamic "ingress" {
    for_each = var.open_npm_admin ? [1] : []
    content {
      from_port   = 81
      to_port     = 81
      protocol    = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
    }
  }

  dynamic "ingress" {
    for_each = var.enable_ssh ? [1] : []
    content {
      from_port   = 22
      to_port     = 22
      protocol    = "tcp"
      cidr_blocks = [var.ssh_cidr]
    }
  }

  dynamic "ingress" {
    for_each = var.expose_mysql ? [1] : []
    content {
      from_port   = 3306
      to_port     = 3306
      protocol    = "tcp"
      cidr_blocks = [var.db_client_cidr]
    }
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
    description = "All outbound"
  }

  tags = merge(local.common_tags, { Name = "${var.prefix}-sg" })
}

########################
# IAM (EC2 + SSM)
########################
resource "aws_iam_role" "ec2_role" {
  name = "${var.prefix}-role-ec2"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect    = "Allow",
      Principal = { Service = "ec2.amazonaws.com" },
      Action    = "sts:AssumeRole"
    }]
  })

  tags = merge(local.common_tags, { Name = "${var.prefix}-role-ec2" })
}

resource "aws_iam_role_policy_attachment" "ssm_core" {
  role       = aws_iam_role.ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

resource "aws_iam_role_policy_attachment" "s3_full" {
  role       = aws_iam_role.ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3FullAccess"
}

resource "aws_iam_instance_profile" "ec2_profile" {
  name = "${var.prefix}-profile-ec2"
  role = aws_iam_role.ec2_role.name
}

########################
# AMI
########################
data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-2023.*-x86_64"]
  }

  filter {
    name   = "architecture"
    values = ["x86_64"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  filter {
    name   = "root-device-type"
    values = ["ebs"]
  }
}

########################
# Key Pair (SSH 열어놓은 경우만)
########################
resource "aws_key_pair" "ec2_key" {
  count      = var.enable_ssh ? 1 : 0
  key_name   = "${var.prefix}-key"
  public_key = file(var.public_key_path)
}

########################
# S3 Bucket (Public Read)
########################
resource "aws_s3_bucket" "public_bucket" {
  bucket = "${var.prefix}-public-bucket"

  tags = merge(local.common_tags, { Name = "${var.prefix}-public-bucket" })
}

# 퍼블릭 액세스 차단 설정을 해제
resource "aws_s3_bucket_public_access_block" "public_bucket_block" {
  bucket = aws_s3_bucket.public_bucket.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

# 버킷 정책 추가: 모든 사용자에게 GetObject (읽기) 권한 부여
resource "aws_s3_bucket_policy" "public_read_policy" {
  bucket = aws_s3_bucket.public_bucket.id

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Sid       = "PublicReadGetObject",
        Effect    = "Allow",
        Principal = "*",
        Action    = "s3:GetObject",
        Resource = ["${aws_s3_bucket.public_bucket.arn}/*"]
      }
    ]
  })

  depends_on = [aws_s3_bucket_public_access_block.public_bucket_block]
}

########################
# EC2
########################
resource "aws_instance" "ec2" {
  ami                         = data.aws_ami.amazon_linux.id
  instance_type               = var.instance_type
  subnet_id                   = aws_subnet.public[0].id
  vpc_security_group_ids      = [aws_security_group.ec2_sg.id]
  associate_public_ip_address = true
  iam_instance_profile        = aws_iam_instance_profile.ec2_profile.name

  key_name = var.enable_ssh ? aws_key_pair.ec2_key[0].key_name : null

  root_block_device {
    volume_type = "gp3"
    volume_size = var.root_volume_size
  }

  user_data = local.ec2_user_data

  tags = merge(local.common_tags, { Name = local.ec2_name_tag })
}

########################
# Elastic IP (EIP)
########################
resource "aws_eip" "ec2_eip" {
  instance = aws_instance.ec2.id

  tags = merge(local.common_tags, { Name = "${var.prefix}-eip" })
}

output "ec2_public_ip" {
  value = aws_eip.ec2_eip.public_ip
}

output "ec2_public_dns" {
  value = aws_eip.ec2_eip.public_dns
}

###############################################
# GitHub Actions OIDC & Deploy Role
# - 목적: GitHub Actions가 AccessKey 없이 STS 단기 자격으로 배포
# - 제한: 특정 repo/branch 에서만 AssumeRole 허용
###############################################
data "aws_caller_identity" "current" {}

data "aws_partition" "current" {}

resource "aws_iam_openid_connect_provider" "github" {
  url             = "https://token.actions.githubusercontent.com"
  client_id_list  = ["sts.amazonaws.com"]
  thumbprint_list = ["6938fd4d98bab03faadb97b34396831e3780aea1"]
}

data "aws_iam_policy_document" "gha_trust" {
  statement {
    effect  = "Allow"
    actions = ["sts:AssumeRoleWithWebIdentity"]

    principals {
      type        = "Federated"
      identifiers = [aws_iam_openid_connect_provider.github.arn]
    }

    condition {
      test     = "StringEquals"
      variable = "token.actions.githubusercontent.com:aud"
      values   = ["sts.amazonaws.com"]
    }

    # 예: repo:our-team/ourlog:ref:refs/heads/main
    condition {
      test     = "StringLike"
      variable = "token.actions.githubusercontent.com:sub"
      values   = ["repo:${var.github_owner}/${var.github_repo}:ref:${var.github_ref}"]
    }
  }
}

resource "aws_iam_role" "gha_deploy" {
  name               = var.gha_role_name
  assume_role_policy = data.aws_iam_policy_document.gha_trust.json

  tags = {
    Name   = var.gha_role_name
    Prefix = var.prefix
  }
}

data "aws_iam_policy_document" "gha_permissions" {
  statement {
    sid     = "EC2Describe"
    effect  = "Allow"
    actions = ["ec2:DescribeInstances"]
    resources = ["*"]
  }

  statement {
    sid     = "SSMSendCommand"
    effect  = "Allow"
    actions = [
      "ssm:SendCommand",
      "ssm:GetCommandInvocation",
      "ssm:ListCommandInvocations"
    ]
    resources = [
      "arn:${data.aws_partition.current.partition}:ec2:${var.region}:${data.aws_caller_identity.current.account_id}:instance/*",
      "arn:${data.aws_partition.current.partition}:ssm:${var.region}::document/AWS-RunShellScript"
    ]
  }
}

resource "aws_iam_role_policy" "gha_deploy_inline" {
  name   = "${var.prefix}-policy-gha-deploy"
  role   = aws_iam_role.gha_deploy.id
  policy = data.aws_iam_policy_document.gha_permissions.json
}

output "gha_role_arn" {
  value = aws_iam_role.gha_deploy.arn
}