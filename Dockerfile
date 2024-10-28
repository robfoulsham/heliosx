# Stage 1: Build the app
FROM maven:3.9.9-amazoncorretto-23 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM amazoncorretto:23
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
