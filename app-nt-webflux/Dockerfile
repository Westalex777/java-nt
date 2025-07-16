FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/app-nt-webflux-*.jar app.jar
EXPOSE 8083
ENV JAVA_TOOL_OPTIONS="-Xmx450m"
ENTRYPOINT ["java", "-jar", "app.jar"]