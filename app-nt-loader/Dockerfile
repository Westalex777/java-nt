FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/app-nt-loader-*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]