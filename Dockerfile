# First stage: build the application
FROM maven:3.8.4-openjdk-17-slim AS builder

# Set the working directory
WORKDIR /app

# Copy the Maven build files into the container
COPY pom.xml .
COPY src ./src

# Build the project and create the jar file
RUN mvn clean package -DskipTests

# Second stage: create a smaller image with only the JAR file
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the generated JAR from the builder stage
COPY --from=builder /app/target/CartService-0.0.1-SNAPSHOT.jar /app/CartService.jar

# Expose port 8092
EXPOSE 8092

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/CartService.jar"]