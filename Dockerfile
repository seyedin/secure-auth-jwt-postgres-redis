# Use an official Java runtime as base image
FROM eclipse-temurin:23-jdk

# Set working directory
WORKDIR /app

# Copy project files and build using Maven
COPY . /app
RUN ./mvnw clean package -DskipTests

# Run the application
CMD ["java", "-jar", "target/secure-auth-0.0.1-SNAPSHOT.jar"]