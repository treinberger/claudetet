#!/bin/bash

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check if Java is installed
if ! command_exists java; then
    echo "Error: Java is not installed. Please install Java 11 or later."
    exit 1
fi

# Check if Maven is installed
if ! command_exists mvn; then
    echo "Error: Maven is not installed. Please install Maven first."
    exit 1
fi

# Function to display usage
show_usage() {
    echo "Usage: ./run-backend.sh [dev|prod]"
    echo "  dev  - Run in development mode (default)"
    echo "  prod - Run in production mode"
}

# Default environment
ENV="dev"

# Parse command line argument
if [ $# -gt 0 ]; then
    case "$1" in
        dev|prod)
            ENV="$1"
            ;;
        -h|--help)
            show_usage
            exit 0
            ;;
        *)
            echo "Invalid argument: $1"
            show_usage
            exit 1
            ;;
    esac
fi

# Clean and build the project
echo "Building the application..."
mvn clean install

# Check if build was successful
if [ $? -ne 0 ]; then
    echo "Build failed. Please check the errors above."
    exit 1
fi

# Run the application based on environment
if [ "$ENV" = "prod" ]; then
    echo "Running in production mode..."
    java -jar -Dspring.profiles.active=prod target/raffle-system-1.0-SNAPSHOT.jar
else
    echo "Running in development mode..."
    java -jar -Dspring.profiles.active=dev target/raffle-system-1.0-SNAPSHOT.jar
fi