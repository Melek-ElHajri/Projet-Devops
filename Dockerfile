# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Expose the application port
EXPOSE 8089

# Add the JAR file to the container
ADD target/tp-foyer-5.0.0.jar tp-foyer-5.0.0.jar

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "/tp-foyer-5.0.0.jar"]
