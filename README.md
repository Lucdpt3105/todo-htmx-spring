<p align="center">
  <img src="assets/banner.png" alt="HTMX + Spring Boot + Thymeleaf + PostgreSQL Railway Template" width="100%">
</p>

<p align="center">
  <strong>Production-ready HTMX starter with Java, Spring Boot, Thymeleaf, and PostgreSQL. One-click deploy to Railway.</strong>
</p>

<p align="center">
  <a href="https://railway.com/deploy/htmxspringthymeleafpostgres">
    <img src="https://railway.com/button.svg" alt="Deploy on Railway">
  </a>
</p>

<p align="center">
  <a href="https://github.com/atoolz/railway-htmx-java-spring-thymeleaf-pg/blob/master/LICENSE">
    <img src="https://img.shields.io/github/license/atoolz/railway-htmx-java-spring-thymeleaf-pg?style=flat-square&color=00c9a7" alt="License">
  </a>
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=flat-square" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.4-6DB33F?style=flat-square" alt="Spring Boot">
  <img src="https://img.shields.io/badge/HTMX-2.0.7-3366CC?style=flat-square" alt="HTMX 2.0.7">
  <img src="https://img.shields.io/badge/PostgreSQL-16-336791?style=flat-square" alt="PostgreSQL">
</p>

<br>

## Deploy and Host HTMX + Spring Boot + PostgreSQL Starter on Railway

HTMX + Spring Boot + PostgreSQL Starter is a production-ready template for hypermedia-driven web apps. It uses HTMX for partial updates, Spring Web + Thymeleaf for server-rendered HTML, JPA + Flyway for persistence, and PostgreSQL. `DATABASE_URL` is parsed from Railway (`postgres://` / `postgresql://`) into a HikariCP datasource. Tailwind and HTMX load from CDN.

### About Hosting

Multi-stage **Dockerfile** (Maven build, JRE runtime). Flyway runs migrations on startup. **`GET /health`** returns JSON and checks the database. **`PORT`** defaults to `8080`.

### Dependencies for Hosting

- Railway **PostgreSQL** (or compatible URL in `DATABASE_URL`)
- `DATABASE_URL` = `${{Postgres.DATABASE_URL}}` on the web service

#### Deployment Dependencies

- [HTMX](https://htmx.org/docs/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Flyway](https://flywaydb.org/)

### Why Deploy on Railway?

Railway hosts your stack with minimal configuration and scales as you grow.

<br>

## What's Inside

| Layer | Technology | Role |
|-------|-----------|------|
| **Frontend** | HTMX 2.0.7 + Tailwind (CDN) | Partial page updates |
| **Templating** | Thymeleaf | SSR + fragments for HTMX swaps |
| **API** | Spring Web | REST + HTML responses |
| **Database** | PostgreSQL + JPA + Flyway | Entities, migrations |

<br>

## Project Structure

```
.
├── src/main/java/com/atoolz/htmx/
│   ├── HtmxApplication.java
│   ├── config/RailwayDataSourceConfig.java
│   └── todo/                    # Entity, repository, controller
├── src/main/resources/
│   ├── application.yaml
│   ├── db/migration/V1__todos.sql
│   └── templates/
│       ├── home.html
│       └── fragments/todo-item.html
├── pom.xml
└── Dockerfile
```

<br>

## HTMX Patterns Demonstrated

- **`hx-post`**, **`hx-patch`**, **`hx-delete`** with `hx-target` / `hx-swap`
- **Health check** — `GET /health`

<br>

## Deploy to Railway

1. Fork this repo (or connect it)
2. New project → add **PostgreSQL**
3. Add a **web** service from this repo (Dockerfile root)
4. Set `DATABASE_URL` = `${{Postgres.DATABASE_URL}}`
5. Health check path: **`/health`**

<br>

## Local Development

```bash
# Java 21 + Maven + local PostgreSQL
export DATABASE_URL="postgresql://user:pass@localhost:5432/mydb"
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080).

<br>

## Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `DATABASE_URL` | Yes | - | PostgreSQL URL (`postgres://` or `postgresql://`) |
| `PORT` | No | `8080` | HTTP port |

<br>

## Part of the HTMX Railway Collection

This is one of 15 HTMX starter templates covering different backend stacks, all following the same pattern and ready for Railway deployment:

| Stack | Status |
|-------|--------|
| Bun + Elysia | Coming soon |
| .NET + Razor | Coming soon |
| Elixir + Phoenix | Coming soon |
| Go + Chi | [Live](https://github.com/atoolz/railway-htmx-go-templ-chi-pg) |
| Go + Echo | [Live](https://github.com/atoolz/railway-htmx-go-templ-echo-pg) |
| Go + Fiber | [Live](https://github.com/atoolz/railway-htmx-go-templ-fiber-pg) |
| Java + Spring Boot (MySQL) | [Live](https://github.com/atoolz/railway-htmx-java-spring-thymeleaf-mysql) |
| **Java + Spring Boot (PostgreSQL)** | **This repo** |
| Node + Express | [Live](https://github.com/atoolz/railway-htmx-node-express-ejs-pg) |
| Node + Hono | [Live](https://github.com/atoolz/railway-htmx-node-hono-jsx-pg) |
| PHP + Laravel | [Live](https://github.com/atoolz/railway-htmx-php-laravel-mysql) |
| Python + Django | [Live](https://github.com/atoolz/railway-htmx-python-django-pg) |
| Python + FastAPI | [Live](https://github.com/atoolz/railway-htmx-python-fastapi-jinja2-pg) |
| Ruby + Rails 8 | [Live](https://github.com/atoolz/railway-htmx-ruby-rails8-pg) |
| Rust + Axum + Askama | [Live](https://github.com/atoolz/railway-htmx-rust-axum-askama-pg) |

<br>

## License

[MIT](LICENSE)

---

<p align="center">
  <sub>Built by <a href="https://github.com/atoolz">AToolZ</a> for the HTMX community</sub>
</p>
