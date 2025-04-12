# Use an official JDK runtime as a parent image
FROM amazoncorretto:17
# Set the working directory in the container
WORKDIR /app
# Copy the application JAR file to the container
COPY target/*.jar app.jar
# Expose the application port
EXPOSE 8080
# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]