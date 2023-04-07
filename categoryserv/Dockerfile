FROM openjdk:17-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy the project JAR file to the container
COPY build/libs/categoryserv-0.0.1-SNAPSHOT.jar app.jar
#build/libs/categoryserv-0.0.1-SNAPSHOT.jar
# Expose port 8080 for the Spring Boot application
EXPOSE 9200

# Run the JAR file when the container launches
CMD ["java", "-jar", "app.jar"]
