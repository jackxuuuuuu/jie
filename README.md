# Jie – 内部请假管理系统 (Practice MVP)

A practice MVP internal leave management system built with **Spring Boot 2.7 / JDK 8**, Spring Security + JWT, MyBatis-Plus, and MySQL.

## Features

| Module | Description |
|---|---|
| Auth | Login with JWT; role-based access control |
| Users | CRUD (HR/ADMIN full; MANAGER own-dept list) |
| Departments | CRUD (ADMIN) |
| Leave | Employee submit; Manager approve/reject; data-scope enforced |
| Permissions | RBAC – ADMIN / HR / MANAGER / EMPLOYEE |

## Tech Stack

- **Java 8** + Spring Boot 2.7.18
- Spring Security + JWT (jjwt 0.11)
- MyBatis-Plus 3.5
- MySQL 8
- SpringDoc OpenAPI (Swagger UI)
- Docker / Docker Compose

## Quickstart

### Prerequisites

- Docker ≥ 20 and Docker Compose v2  
  _or_ JDK 8 + Maven 3.8 + MySQL 8 locally

### 1 – Start with Docker Compose (recommended)

```bash
git clone https://github.com/jackxuuuuuu/jie.git
cd jie
docker compose up --build
```

Wait for the `jie-backend` container to print `Started JieApplication`. Then open:

| URL | Description |
|---|---|
| http://localhost:8080/swagger-ui.html | Swagger UI |
| http://localhost:8080/api/auth/login | Login endpoint |

### 2 – Start locally (without Docker)

1. Create the database:
   ```sql
   CREATE DATABASE jie_db CHARACTER SET utf8mb4;
   CREATE USER 'jie'@'localhost' IDENTIFIED BY 'jie123';
   GRANT ALL ON jie_db.* TO 'jie'@'localhost';
   ```
2. Build and run:
   ```bash
   cd backend
   mvn spring-boot:run
   ```

The schema and seed data are applied automatically on first start via `spring.sql.init`.

## Default Accounts

| Username | Password | Role |
|---|---|---|
| admin | Jie@1234 | ADMIN |
| hr01 | Jie@1234 | HR |
| mgr_ck | Jie@1234 | MANAGER (仓储部) |
| emp01 | Jie@1234 | EMPLOYEE |
| emp02 | Jie@1234 | EMPLOYEE |

## API Quick Reference

### Auth
```
POST /api/auth/login
```
```json
{ "username": "admin", "password": "Jie@1234" }
```
Returns `{ "data": { "token": "...", "userId": 1, "realName": "系统管理员", "roleCodes": ["ADMIN"] } }`

Use the token in subsequent requests:
```
Authorization: Bearer <token>
```

### Departments
```
GET    /api/depts           # all active depts (any authenticated user)
POST   /api/depts           # create (ADMIN)
PUT    /api/depts/{id}      # update (ADMIN)
DELETE /api/depts/{id}      # delete (ADMIN)
```

### Users
```
GET /api/users              # list (ADMIN/HR=all; MANAGER=own dept)
GET /api/users/{id}
POST /api/users             # create (ADMIN/HR)
PUT  /api/users/{id}        # update (ADMIN/HR)
PUT  /api/users/{id}/disable
PUT  /api/users/{id}/enable
PUT  /api/users/{id}/reset-password
```

### Leave
```
POST /api/leave             # submit (any authenticated user)
GET  /api/leave/mine        # my own records
GET  /api/leave             # list (ADMIN/HR=all; MANAGER=own dept)
GET  /api/leave/{id}
POST /api/leave/{id}/approve   # (ADMIN/HR/MANAGER)
POST /api/leave/{id}/reject    # (ADMIN/HR/MANAGER)
POST /api/leave/{id}/cancel    # applicant only
```

## Data Scope Rules

| Role | Users | Leave Records |
|---|---|---|
| ADMIN | All | All |
| HR | All | All |
| MANAGER | Own dept only | Own dept only |
| EMPLOYEE | Own profile | Own records only |

## Project Structure

```
jie/
├── backend/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/jie/
│       ├── JieApplication.java
│       ├── common/          # Result, PageResult, BizException, GlobalExceptionHandler
│       ├── config/          # MybatisPlusConfig, SwaggerConfig
│       ├── security/        # JwtUtils, JwtAuthFilter, SecurityConfig, LoginUser
│       └── module/
│           ├── auth/        # AuthController + DTOs
│           ├── org/
│           │   ├── dept/    # Dept CRUD
│           │   ├── user/    # SysUser CRUD
│           │   └── role/    # SysRole, SysUserRole
│           └── leave/       # Leave submit/approve/reject
├── docker-compose.yml
└── README.md
```