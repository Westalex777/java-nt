FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/app-nt-mock-*.jar app.jar
EXPOSE 8080
ENV JAVA_TOOL_OPTIONS="-Xmx1500m"
ENTRYPOINT ["java", "-jar", "app.jar"]