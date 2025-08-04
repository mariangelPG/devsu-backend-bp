# Dockerfile ms-cuentas

# Multi-stage build para Java 21 + Gradle
FROM openjdk:21-jdk-slim as build

# Instalar herramientas necesarias
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# Dar permisos de ejecución
RUN chmod +x ./gradlew

# Descargar dependencias (layer cacheable)
RUN ./gradlew dependencies --no-daemon || true

# Copiar código fuente
COPY src src

# Build del proyecto
RUN ./gradlew clean bootJar --no-daemon --info

# Verificar que el JAR se generó
RUN ls -la build/libs/

# Stage 2: Runtime
FROM openjdk:21-jdk-slim

# Crear usuario no-root
RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

COPY --from=build /app/src/main/resources/application.properties ./
COPY --from=build /app/build/libs/*.jar app.jar
RUN mkdir logs
RUN chown spring:spring /app/logs

# Cambiar al usuario no-root
USER spring:spring

# Puerto por defecto
EXPOSE 8082

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]