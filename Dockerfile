# Multi-stage Dockerfile for the Student API
# Author: Ablekimov Alisher (ID 37637, group IT2-2312)
# Build:  docker build -t ablekimov-alisher-student-api:v1.0-ablekimov-alisher .

# ---------- Stage 1: build the JAR with Gradle ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Copy Gradle wrapper and build files first, so dependencies are cached between builds
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
# sed removes Windows line endings (CRLF) in case the file was edited on Windows
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew && ./gradlew --no-daemon dependencies -q

# Copy the source code and build the executable JAR (tests run in Jenkins, not here)
COPY src ./src
RUN ./gradlew --no-daemon clean bootJar -x test

# ---------- Stage 2: small runtime image (JRE only, no Gradle, no source code) ----------
FROM eclipse-temurin:21-jre

ARG APP_TAG=v1.0-ablekimov-alisher
LABEL org.opencontainers.image.title="ablekimov-alisher-student-api" \
      org.opencontainers.image.version="${APP_TAG}" \
      org.opencontainers.image.authors="Ablekimov Alisher (ID 37637, IT2-2312)" \
      org.opencontainers.image.description="Spring Boot CRUD Student API"

WORKDIR /app

# Run the application as a non-root user (good security practice)
RUN groupadd --system spring && useradd --system --gid spring spring

COPY --from=build /workspace/build/libs/*.jar /app/app.jar
RUN chown spring:spring /app/app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
