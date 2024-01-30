# Md. Tanver Ahammed
# Dockerfile for Note-Taker multi-stage build
# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create a minimal JRE image
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/Note-Taker-0.0.1-SNAPSHOT.jar Note-Taker-0.0.1-SNAPSHOT.jar

# Specify the default command to run on startup
CMD ["java", "-jar", "Note-Taker-0.0.1-SNAPSHOT.jar"]