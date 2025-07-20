#!/bin/bash

set -e  # Exit on any error

echo "Starting Minilinkify URL API..."
echo "Java version:"
java -version

# Check if jar file exists
if [ ! -f "app.jar" ]; then
    echo "Error: app.jar not found!"
    exit 1
fi

echo "Starting application with JAVA_OPTS: $JAVA_OPTS"

# Start the application
exec java $JAVA_OPTS -jar app.jar
