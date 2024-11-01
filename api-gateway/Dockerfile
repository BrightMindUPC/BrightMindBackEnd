# Etapa de construcción
FROM maven:3.8.3-openjdk-17-slim AS build

WORKDIR /app

# Copiamos pom.xml y descargamos las dependencias para aprovechar la capa de caché de Docker
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copiamos el código fuente y compilamos el proyecto
COPY src ./src
RUN mvn clean package

# Etapa de ejecución
FROM amazoncorretto:17-alpine-jdk

WORKDIR /app
ENV PORT=8081
ENV LOGGING_LEVEL=INFO

# Copiamos el JAR de la etapa de construcción
COPY --from=build /app/target/*.jar ./app.jar

# Configuramos el punto de entrada para iniciar la aplicación de Spring Boot
EXPOSE ${PORT}
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]