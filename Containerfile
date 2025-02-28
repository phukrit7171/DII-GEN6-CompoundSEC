# Use an official JRE base image
FROM docker.io/library/eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY app/build/libs/app-uber.jar /app/

# Make port 8080 available if needed in the future
EXPOSE 8080

# Set both the entry point and default command so it can be overridden for testing
ENTRYPOINT ["java", "-jar", "app-uber.jar"]
CMD []
