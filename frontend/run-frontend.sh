#!/bin/bash

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check if Node.js is installed
if ! command_exists node; then
    echo "Error: Node.js is not installed. Please install Node.js first."
    exit 1
fi

# Check if npm is installed
if ! command_exists npm; then
    echo "Error: npm is not installed. Please install npm first."
    exit 1
fi

# Function to display usage
show_usage() {
    echo "Usage: ./run-frontend.sh [dev|prod]"
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

# Install dependencies if node_modules doesn't exist
if [ ! -d "node_modules" ]; then
    echo "Installing dependencies..."
    npm install
fi

# Run the application based on environment
if [ "$ENV" = "prod" ]; then
    echo "Building and running in production mode..."
    npm run build:prod && npm run serve:dist
else
    echo "Running in development mode..."
    npm run start:dev
fi