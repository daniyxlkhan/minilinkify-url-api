#!/bin/bash

set -e  # Exit on any error

echo "=== Building Minilinkify URL API ==="

# Check Java version
echo "Java version:"
java -version 2>&1

# Clean any previous builds
echo "Cleaning previous builds..."
rm -f app.jar

# Try to use Maven wrapper first, fall back to system Maven
if [ -x "./mvnw" ]; then
    echo "Using Maven wrapper..."
    chmod +x ./mvnw
    ./mvnw clean package -DskipTests -q
else
    echo "Maven wrapper not found, using system Maven..."
    mvn clean package -DskipTests -q
fi

echo "Build completed, looking for JAR files..."
ls -la target/

# Find and copy the built jar with priority order
if [ -f "target/minilinkify.jar" ]; then
    cp target/minilinkify.jar app.jar
    echo "✅ Found and copied: minilinkify.jar"
elif [ -f "target/demo-0.0.1-SNAPSHOT.jar" ]; then
    cp target/demo-0.0.1-SNAPSHOT.jar app.jar
    echo "✅ Found and copied: demo-0.0.1-SNAPSHOT.jar"
else
    # Find any executable jar file in target directory
    JAR_FILE=$(find target -name "*.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" -not -name "*.original" | head -n 1)
    if [ -n "$JAR_FILE" ]; then
        cp "$JAR_FILE" app.jar
        echo "✅ Found and copied: $JAR_FILE"
    else
        echo "❌ No jar file found in target directory!"
        echo "Target directory contents:"
        ls -la target/ || echo "Target directory doesn't exist"
        exit 1
    fi
fi

# Verify the jar file
if [ -f "app.jar" ]; then
    echo "✅ Build successful! app.jar created:"
    ls -lh app.jar
else
    echo "❌ Build failed - app.jar not found"
    exit 1
fi
