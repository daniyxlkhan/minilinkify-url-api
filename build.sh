#!/bin/bash

set -e  # Exit on any error

echo "Building Minilinkify URL API..."

# Try to use Maven wrapper first, fall back to system Maven
if [ -x "./mvnw" ]; then
    echo "Using Maven wrapper..."
    ./mvnw clean package -DskipTests
else
    echo "Maven wrapper not found, using system Maven..."
    mvn clean package -DskipTests
fi

# Find and copy the built jar
if [ -f target/minilinkify.jar ]; then
    cp target/minilinkify.jar app.jar
    echo "Found minilinkify.jar"
elif [ -f target/*-SNAPSHOT.jar ]; then
    cp target/*-SNAPSHOT.jar app.jar
    echo "Found SNAPSHOT jar"
else
    # Find any jar file in target directory
    JAR_FILE=$(find target -name "*.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" | head -n 1)
    if [ -n "$JAR_FILE" ]; then
        cp "$JAR_FILE" app.jar
        echo "Found jar: $JAR_FILE"
    else
        echo "No jar file found in target directory!"
        exit 1
    fi
fi

echo "Build completed successfully!"
ls -la app.jar
