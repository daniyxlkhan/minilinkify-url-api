#!/bin/bash

set -e  # Exit on any error

echo "=== Starting Minilinkify URL API ==="

# Check if jar file exists
if [ ! -f "app.jar" ]; then
    echo "❌ Error: app.jar not found!"
    echo "Current directory contents:"
    ls -la
    exit 1
fi

echo "✅ Found app.jar"
ls -lh app.jar

# Check Java version
echo "Java version:"
java -version 2>&1

# Display environment info
echo "Environment variables:"
echo "PORT: ${PORT:-not set}"
echo "JAVA_OPTS: ${JAVA_OPTS:-not set}"
echo "DATABASE_URL: ${DATABASE_URL:0:50}..." # Show first 50 chars only for security

echo "🚀 Starting application..."

# Start the application
exec java ${JAVA_OPTS} -Dserver.port=${PORT:-8080} -jar app.jar
