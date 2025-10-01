# === Build ===
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests validate

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests package

# === Runtime ===
FROM eclipse-temurin:17-jre
WORKDIR /app
# Ajusta el puerto si usas otro (p.ej., 8081)
COPY --from=build /workspace/target/*-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
