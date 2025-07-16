FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/app-nt-platform-thread-*.jar app.jar
EXPOSE 8081
ENV JAVA_TOOL_OPTIONS="-Xmx450m"
ENTRYPOINT ["java", "-jar", "app.jar"]