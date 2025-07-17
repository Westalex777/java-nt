FROM gradle:8.4-jdk21 AS builder
COPY . /app
WORKDIR /app
RUN gradle clean build --no-daemon

FROM eclipse-temurin:21-jre
COPY --from=builder /app/build/libs/ /app/libs/
EXPOSE 8085
WORKDIR /app
CMD ["java", "-cp", "libs/*", "MainKt"]
