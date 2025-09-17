# === Build ===
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# === Runtime ===
FROM eclipse-temurin:17-jre
WORKDIR /app
# usa comodín por si cambia el nombre del jar
COPY --from=build /workspace/target/*-SNAPSHOT.jar app.jar
# ojo: si este micro expone 8081, cámbialo aquí
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
