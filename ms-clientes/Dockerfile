# Dockerfile ms-clientes

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

# Copiar el JAR desde el stage de build
COPY --from=build /app/build/libs/*.jar app.jar

# Cambiar al usuario no-root
USER spring:spring

# Puerto por defecto
EXPOSE 8081

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]