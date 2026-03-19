# ── Étape 1 : Build Maven ─────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Cache des dépendances Maven
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Compilation
COPY src ./src
RUN mvn clean package -DskipTests

# ── Étape 2 : Image finale légère ─────────────────────────────────
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/demo.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
