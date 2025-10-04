# Use an official OpenJDK image as base
FROM openjdk:17-jdk-slim

# Optional: Set a working directory inside the container
WORKDIR /app

# Copy Maven built jar into the container
COPY target/*.jar app.jar

# Expose port your Spring Boot app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-jar","app.jar"]
