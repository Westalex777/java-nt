FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/app-nt-mock-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]