terraform {
  # aws 라이브러리 불러옴
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

# AWS 설정 시작
provider "aws" {
  region     = var.region
  access_key = var.accessKey
  secret_key = var.secretKey
}
# AWS 설정 끝

# VPC 설정 시작
resource "aws_vpc" "vpc_1" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = {
    Name = "${var.prefix}-vpc-1"
  }
}
# VPC 설정 끝

# --------------------------------------------------
# 공용 서브넷 생성 (각 AZ별)
# --------------------------------------------------

# 가용 영역 1: us-east-1a - 공용 서브넷 (서브넷 a)
resource "aws_subnet" "subnet_a" {
  vpc_id                  = aws_vpc.vpc_1.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "${var.region}a"
  map_public_ip_on_launch = true

  tags = {
    Name = "${var.prefix}-subnet-a"
  }
}

# 가용 영역 2: us-east-1b - 공용 서브넷 (서브넷 c)
resource "aws_subnet" "subnet_c" {
  vpc_id                  = aws_vpc.vpc_1.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "${var.region}b"
  map_public_ip_on_launch = true

  tags = {
    Name = "${var.prefix}-subnet-c"
  }
}

# --------------------------------------------------
# 사설 서브넷 생성 (각 AZ별)
# --------------------------------------------------

# 가용 영역 1: us-east-1a - 사설 서브넷 (서브넷 b)
resource "aws_subnet" "subnet_b" {
  vpc_id            = aws_vpc.vpc_1.id
  cidr_block        = "10.0.101.0/24"
  availability_zone = "${var.region}a"
  map_public_ip_on_launch = false

  tags = {
    Name = "${var.prefix}-subnet-b"
  }
}

# 가용 영역 2: us-east-1b - 사설 서브넷 (서브넷 d)
resource "aws_subnet" "subnet_d" {
  vpc_id            = aws_vpc.vpc_1.id
  cidr_block        = "10.0.102.0/24"
  availability_zone = "${var.region}b"
  map_public_ip_on_launch = false

  tags = {
    Name = "${var.prefix}-subnet-d"
  }
}

# --------------------------------------------------
# 인터넷 게이트웨이 (IGW)
# --------------------------------------------------
resource "aws_internet_gateway" "igw_1" {
  vpc_id = aws_vpc.vpc_1.id

  tags = {
    Name = "${var.prefix}-igw-1"
  }
}

# --------------------------------------------------
# 공용 라우팅 테이블 (IGW 연결)
# --------------------------------------------------
resource "aws_route_table" "rt_public" {
  vpc_id = aws_vpc.vpc_1.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw_1.id
  }

  tags = {
    Name = "${var.prefix}-rt-public"
  }
}

# 공용 서브넷 a와 c에 라우트 테이블 연결
resource "aws_route_table_association" "public_assoc_a" {
  subnet_id      = aws_subnet.subnet_a.id
  route_table_id = aws_route_table.rt_public.id
}

resource "aws_route_table_association" "public_assoc_c" {
  subnet_id      = aws_subnet.subnet_c.id
  route_table_id = aws_route_table.rt_public.id
}

# --------------------------------------------------
# NAT 게이트웨이 구성
# 각 AZ의 공용 서브넷에 NAT EIP와 NAT 게이트웨이를 생성하여 사설 서브넷에서 아웃바운드 인터넷 접근 지원
# --------------------------------------------------

# NAT 게이트웨이 (AZ a용)
resource "aws_eip" "nat_eip_a" {
  tags = {
    Name = "${var.prefix}-nat-eip-a"
  }
}

resource "aws_nat_gateway" "nat_gw_a" {
  allocation_id = aws_eip.nat_eip_a.id
  subnet_id     = aws_subnet.subnet_a.id

  tags = {
    Name = "${var.prefix}-nat-gw-a"
  }
}

# NAT 게이트웨이 (AZ b용)
resource "aws_eip" "nat_eip_b" {
  tags = {
    Name = "${var.prefix}-nat-eip-b"
  }
}

resource "aws_nat_gateway" "nat_gw_b" {
  allocation_id = aws_eip.nat_eip_b.id
  subnet_id     = aws_subnet.subnet_c.id
  tags = {
    Name = "${var.prefix}-nat-gw-b"
  }
}

# --------------------------------------------------
# 사설 라우팅 테이블 생성 (각 AZ별)
# --------------------------------------------------

# 사설 라우팅 테이블 for AZ a
resource "aws_route_table" "rt_private_a" {
  vpc_id = aws_vpc.vpc_1.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat_gw_a.id
  }

  tags = {
    Name = "${var.prefix}-rt-private-a"
  }
}

resource "aws_route_table_association" "private_assoc_b" {
  subnet_id      = aws_subnet.subnet_b.id
  route_table_id = aws_route_table.rt_private_a.id
}

# 사설 라우팅 테이블 for AZ b
resource "aws_route_table" "rt_private_b" {
  vpc_id = aws_vpc.vpc_1.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat_gw_b.id
  }

  tags = {
    Name = "${var.prefix}-rt-private-b"
  }
}

resource "aws_route_table_association" "private_assoc_d" {
  subnet_id      = aws_subnet.subnet_d.id
  route_table_id = aws_route_table.rt_private_b.id
}

# --------------------------------------------------
# 보안 그룹 구성
# --------------------------------------------------
resource "aws_security_group" "sg_1" {
  name   = "${var.prefix}-sg-1"
  vpc_id = aws_vpc.vpc_1.id

  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "all"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.prefix}-sg-1"
  }
}

resource "aws_security_group" "rds_sg" {
  name   = "${var.prefix}-rds-sg"
  vpc_id = aws_vpc.vpc_1.id

  ingress {
    description     = "Allow MySQL from EC2"
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.sg_1.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.prefix}-rds-sg"
  }
}

resource "aws_security_group" "docdb_sg" {
  name        = "${var.prefix}-docdb-sg"
  description = "Allow access to DocumentDB from EC2"
  vpc_id      = aws_vpc.vpc_1.id

  ingress {
    description     = "Allow DocumentDB access from EC2 SG"
    from_port       = 27017
    to_port         = 27017
    protocol        = "tcp"
    security_groups = [aws_security_group.sg_1.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.prefix}-docdb-sg"
  }
}

# --------------------------------------------------
# EC2, RDS, DocumentDB 및 기타 리소스 설정 (변경 없음)
# --------------------------------------------------

# EC2 역할 및 인스턴스 (EC2는 공용 서브넷 a 또는 c 중 하나에 배치)
resource "aws_iam_role" "ec2_role_1" {
  name = "${var.prefix}-ec2-role-1"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
          "Service": "ec2.amazonaws.com"
      },
      "Effect": "Allow"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "s3_full_access" {
  role       = aws_iam_role.ec2_role_1.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3FullAccess"
}

resource "aws_iam_role_policy_attachment" "ec2_ssm" {
  role       = aws_iam_role.ec2_role_1.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2RoleforSSM"
}

resource "aws_iam_role_policy_attachment" "documentdb" {
  role       = aws_iam_role.ec2_role_1.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonDocDBFullAccess"
}

resource "aws_iam_role_policy_attachment" "documentdb_elastic" {
  role       = aws_iam_role.ec2_role_1.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonDocDBElasticFullAccess"
}

resource "aws_iam_role_policy_attachment" "ec2_ssm2" {
  role       = aws_iam_role.ec2_role_1.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

resource "aws_iam_instance_profile" "instance_profile_1" {
  name = "${var.prefix}-instance-profile-1"
  role = aws_iam_role.ec2_role_1.name
}

locals {
  ec2_user_data_base = <<-END_OF_FILE
#!/bin/bash
# Docker 설치 및 실행
yum install -y docker
systemctl enable docker
systemctl start docker

# Docker Compose 설치
curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# Git 설치
yum install -y git

# Swap 파일 생성 및 활성화
dd if=/dev/zero of=/swapfile bs=128M count=32
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
sudo swapon -s
sudo sh -c 'echo "/swapfile swap swap defaults 0 0" >> /etc/fstab'

END_OF_FILE
}

# 예제에서는 us-east-1a의 공용 서브넷 a에 EC2를 배치합니다.
resource "aws_instance" "ec2_1" {
  ami                         = "ami-0cee4e6a7532bb297"
  instance_type               = "t2.micro"
  subnet_id                   = aws_subnet.subnet_a.id
  vpc_security_group_ids      = [aws_security_group.sg_1.id]
  associate_public_ip_address = true
  iam_instance_profile        = aws_iam_instance_profile.instance_profile_1.name
  key_name                    = "smore-key"

  tags = {
    Name = "${var.prefix}-ec2-1"
  }

  root_block_device {
    volume_type = "gp3"
    volume_size = 30
  }

  user_data = <<-EOF
${local.ec2_user_data_base}
EOF
}

# # DocumentDB Elastic 클러스터 (사설 서브넷: AZ a의 서브넷 b와 AZ b의 서브넷 d 사용)
# resource "aws_docdbelastic_cluster" "docdbelastic_cluster" {
#   name                = "${var.prefix}-docdbelastic-cluster"
#   admin_user_name     = var.DBUser
#   admin_user_password = var.DBPassword
#   auth_type           = "PLAIN_TEXT"
#   shard_capacity      = 2
#   shard_count         = 1
#
#   subnet_ids             = [aws_subnet.subnet_b.id, aws_subnet.subnet_d.id]
#   vpc_security_group_ids = [aws_security_group.docdb_sg.id]
#
#   tags = {
#     Name = "${var.prefix}-docdbelastic-cluster"
#   }
# }

# RDS를 위한 DB 서브넷 그룹 (사설 서브넷: 서브넷 b와 d)
resource "aws_db_subnet_group" "rds_subnet_group" {
  name       = "${var.prefix}-rds-subnet-group"
  subnet_ids = [
    aws_subnet.subnet_b.id,
    aws_subnet.subnet_d.id
  ]

  tags = {
    Name = "${var.prefix}-rds-subnet-group"
  }
}

# RDS 인스턴스 생성 (MySQL, 사설 서브넷에 배치)
resource "aws_db_instance" "rds_instance" {
  identifier             = "${var.prefix}-rds-instance"
  allocated_storage      = 20
  storage_type           = "gp2"
  engine                 = "mysql"
  engine_version         = "8.0.40"
  instance_class         = "db.t4g.micro"
  username               = var.DBUser
  password               = var.DBPassword
  db_name = "smore"
  db_subnet_group_name   = aws_db_subnet_group.rds_subnet_group.name
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  skip_final_snapshot    = true
  publicly_accessible    = false

  tags = {
    Name = "${var.prefix}-rds-instance"
  }
}

###############################################
# Redis 연결을 위한 추가 Terraform 코드 예제 #
###############################################

# Redis용 보안 그룹: EC2에서 오는 접근(6379 포트)만 허용
resource "aws_security_group" "redis_sg" {
  name   = "${var.prefix}-redis-sg"
  vpc_id = aws_vpc.vpc_1.id

  ingress {
    description     = "Allow Redis access from EC2"
    from_port       = 6379
    to_port         = 6379
    protocol        = "tcp"
    security_groups = [aws_security_group.sg_1.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.prefix}-redis-sg"
  }
}

# Redis용 ElastiCache 서브넷 그룹: 사설 서브넷 사용
resource "aws_elasticache_subnet_group" "redis_subnet_group" {
  name       = "${var.prefix}-redis-subnet-group"
  subnet_ids = [
    aws_subnet.subnet_b.id,
    aws_subnet.subnet_d.id
  ]

  tags = {
    Name = "${var.prefix}-redis-subnet-group"
  }
}

# Redis 클러스터 생성 (단일 노드 Redis)
resource "aws_elasticache_cluster" "redis_cluster" {
  cluster_id           = "${var.prefix}-redis-cluster"
  engine               = "redis"
  node_type            = "cache.t3.micro"          # 필요에 따라 노드 타입 변경 가능
  num_cache_nodes      = 1                         # Redis는 반드시 1이어야 함
  parameter_group_name = "default.redis6.x"        # 사용 환경에 맞게 조정 가능
  engine_version       = "6.x"                     # 사용 환경에 맞게 조정 가능
  port                 = 6379
  subnet_group_name    = aws_elasticache_subnet_group.redis_subnet_group.name
  security_group_ids   = [aws_security_group.redis_sg.id]
  apply_immediately    = true

  tags = {
    Name = "${var.prefix}-redis-cluster"
  }
}

# Redis 엔드포인트와 포트를 출력 (EC2에서 접속 시 활용)
output "redis_endpoint" {
  description = "Redis cluster endpoint"
  value       = aws_elasticache_cluster.redis_cluster.cache_nodes[0].address
}

output "redis_port" {
  description = "Redis cluster port"
  value       = aws_elasticache_cluster.redis_cluster.port
}

# S3 설정 시작
resource "aws_s3_bucket" "bucket_1" {
  bucket = "${var.prefix}-bucket-public-1"

  tags = {
    Name = "${var.prefix}-bucket-public-1"
  }
}

data "aws_iam_policy_document" "bucket_1_policy_1_statement" {
  statement {
    sid    = "PublicReadGetObject"
    effect = "Allow"

    principals {
      type        = "AWS"
      identifiers = ["*"]
    }

    actions   = ["s3:GetObject"]
    resources = ["${aws_s3_bucket.bucket_1.arn}/*"]
  }
}

resource "aws_s3_bucket_policy" "bucket_1_policy_1" {
  bucket = aws_s3_bucket.bucket_1.id

  policy = data.aws_iam_policy_document.bucket_1_policy_1_statement.json

  depends_on = [aws_s3_bucket_public_access_block.bucket_1_public_access_block_1]
}

resource "aws_s3_bucket_public_access_block" "bucket_1_public_access_block_1" {
  bucket = aws_s3_bucket.bucket_1.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}

resource "aws_s3_bucket" "bucket_2" {
  bucket = "${var.prefix}-bucket-private-1"

  tags = {
    Name = "${var.prefix}-bucket-private-1"
  }
}

resource "aws_s3_object" "object_1" {
  bucket       = aws_s3_bucket.bucket_1.id
  key          = "/index.html"
  content      = "Hello"  # 직접 문자열 사용
  content_type = "text/html"
  etag       = md5("Hello")
  depends_on = [aws_s3_bucket.bucket_2]
}

resource "aws_s3_object" "object_2" {
  bucket       = aws_s3_bucket.bucket_2.id
  key          = "/index.html"
  content      = "Hello"  # 직접 문자열 사용
  content_type = "text/html"
  etag       = md5("Hello")
  depends_on = [aws_s3_bucket.bucket_2]
}
# S3 설정 끝