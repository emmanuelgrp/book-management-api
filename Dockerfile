# Stage 1: Build
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar pom y código fuente
COPY pom.xml .
COPY src ./src

# Compilar JAR sin tests
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copiar JAR compilado
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Ejecutar la app con perfil prod
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=prod"]