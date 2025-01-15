# Use a base image with Java
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Information around who maintains the image
LABEL org.opencontainers.image.authors="Stepan"

# Copy the JAR file
COPY target/Appliance-store-Spring-0.0.1-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]