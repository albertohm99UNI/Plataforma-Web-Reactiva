
FROM eclipse-temurin:23-jre-alpine

# Crear un directorio para la aplicación
WORKDIR /app

# Copiar el archivo JAR de la aplicación al contenedor
COPY target/*.jar gestion-app.jar

# Exponer el puerto en el que la aplicación se ejecuta
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-Dspring.profiles.active=despliegue", "-jar", "gestion-app.jar"]
