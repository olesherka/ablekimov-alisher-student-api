# Student API — Ablekimov Alisher

Spring Boot CRUD application for managing students. DevOps midterm project.

- **Author:** Ablekimov Alisher
- **Student ID:** 37637
- **Group:** IT2-2312
- **GitHub:** https://github.com/olesherka

HEAD
Project status: CRUD API ready for Docker (Ablekimov Alisher)
Project status: documentation reviewed by Ablekimov Alisher
>>>>>>> feature/ablekimov-alisher-documentation

## Tech stack

Java 21, Spring Boot 3.5, Spring Data JPA, PostgreSQL 16, Gradle, Docker, Docker Compose, Jenkins.

## Project structure

```
ablekimov-alisher-student-api/
├── scripts/      Bash script Ablekimov_Alisher_system_report.sh
├── logs/         system report log and log analysis
├── backups/      README backup
├── src/          Spring Boot source code and tests
├── jenkins/      local Jenkins server (Docker)
├── Dockerfile    multi-stage image build
├── docker-compose.yml
├── Jenkinsfile
├── build.gradle
└── README.md
```

## Run with Docker Compose

```bash
cp .env.example .env        # set your own password in .env
docker compose up -d --build
```

Application URL: http://localhost:8080/ablekimov-alisher

## API endpoints

| Method | URL | Description |
|--------|-----|-------------|
| GET    | /ablekimov-alisher/ | Author and version info |
| GET    | /ablekimov-alisher/api/students | Get all students |
| GET    | /ablekimov-alisher/api/students/{id} | Get one student |
| POST   | /ablekimov-alisher/api/students | Create a student |
| PUT    | /ablekimov-alisher/api/students/{id} | Update a student |
| DELETE | /ablekimov-alisher/api/students/{id} | Delete a student |

Example student JSON:

```json
{"firstName":"Alisher","lastName":"Ablekimov","email":"alisher.ablekimov.37637@example.com","groupName":"IT2-2312"}
```

## Docker image

`ablekimov-alisher-student-api:v1.0-ablekimov-alisher`

## Changelog

- feature/ablekimov-alisher-crud (Ablekimov Alisher): added GET /api/students/search?lastName=... to find students by part of the last name (case-insensitive), with tests.
- CI trigger test by Ablekimov Alisher on 2026-10-30 02:09
