FROM eclipse-temurin:24-jdk-alpine-3.20
WORKDIR /app
COPY *.jar tiempo.jar
EXPOSE 8080
CMD ["java", "-Dspring.profiles.active=despliegue", "-jar", "tiempo.jar"]