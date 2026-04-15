<p align="center">
  <img src="assets/banner.png" alt="HTMX + Spring Boot + Thymeleaf + PostgreSQL Railway Template" width="100%">
</p>

<p align="center">
  <strong>Production-ready HTMX todo app with Java, Spring Boot, Thymeleaf, and PostgreSQL. Deploy to Railway in one click.</strong>
</p>

<p align="center">
  <a href="https://railway.com/deploy/htmxspringthymeleafpostgres">
    <img src="https://railway.com/button.svg" alt="Deploy on Railway">
  </a>
  &nbsp;
  <a href="https://github.com/atoolz/railway-htmx-java-spring-thymeleaf-pg/blob/master/LICENSE">
    <img src="https://img.shields.io/github/license/atoolz/railway-htmx-java-spring-thymeleaf-pg?style=flat-square&color=00c9a7" alt="License">
  </a>
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=flat-square" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.4-6DB33F?style=flat-square" alt="Spring Boot">
  <img src="https://img.shields.io/badge/HTMX-2.0.7-3366CC?style=flat-square" alt="HTMX 2.0.7">
  <img src="https://img.shields.io/badge/PostgreSQL-16-336791?style=flat-square" alt="PostgreSQL">
</p>

<br>

## Giới thiệu

Một todo app hiện đại với HTMX cho dynamic interactions, Spring Boot cho REST API, Thymeleaf cho server-side rendering, và PostgreSQL làm database. Hỗ trợ Docker, Flyway migrations, và sẵn sàng deploy trên Railway.

## Công nghệ

| Tầng | Công nghệ | Mục đích |
|------|-----------|---------|
| **Frontend** | HTMX 2.0.7 + Tailwind (CDN) | Dynamic partial updates |
| **Templating** | Thymeleaf | Server-side rendering + HTMX fragments |
| **Backend** | Spring Boot 3.4 + Spring Web | REST API |
| **Database** | PostgreSQL 16 + JPA + Flyway | ORM + migrations |

## Cấu trúc Project

```
src/main/java/com/atoolz/htmx/
├── HtmxApplication.java
├── config/RailwayDataSourceConfig.java
└── todo/                          # Entity, repository, controller
    ├── Todo.java
    ├── TodoRepository.java
    └── TodoController.java

src/main/resources/
├── application.yaml
├── db/migration/V1__todos.sql
└── templates/
    ├── home.html
    └── fragments/todo-item.html
```

## Features

- ✅ Dynamic todo list (create, update, delete) với HTMX - không reload page
- ✅ Server-side rendering bằng Thymeleaf
- ✅ REST API với Spring Boot
- ✅ Database migrations với Flyway
- ✅ Health check endpoint (`GET /health`)
- ✅ Docker + Docker Compose support
- ✅ One-click deploy trên Railway

## Deploy to Railway

1. Fork repo này hoặc kết nối với Railway
2. New project → thêm **PostgreSQL** plugin
3. Thêm **web service** từ repo (Dockerfile ở root)
4. Set `DATABASE_URL = ${{Postgres.DATABASE_URL}}`
5. Health check: **`/health`**

## Local Development

```bash
# Cần: Java 21, Maven, PostgreSQL
export DATABASE_URL="postgresql://user:pass@localhost:5432/mydb"
mvn spring-boot:run
```

Mở [http://localhost:8080](http://localhost:8080)

## Biến môi trường

| Biến | Bắt buộc | Mặc định | Chi tiết |
|------|----------|---------|---------|
| `DATABASE_URL` | Yes | - | PostgreSQL URL (postgres:// hoặc postgresql://); trên Railway dùng `${{Postgres.DATABASE_URL}}` |
| `PORT` | No | `8080` | HTTP port |

<br>

## License

[MIT](LICENSE)

---

<p align="center">
  <sub>Built by <a href="https://github.com/atoolz">AToolZ</a> for the HTMX community</sub>
</p>
