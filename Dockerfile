FROM openjdk:17-jdk-slim

# Set environment variables
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS=""

# Seting working directory
WORKDIR /app

# Copy the jar into the container
COPY target/minilinkify.jar app.jar

# Run the jar file
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
