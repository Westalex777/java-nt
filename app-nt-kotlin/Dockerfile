FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/app-nt-kotlin-*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]