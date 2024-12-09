#!/bin/bash

# Output file
OUTPUT_FILE="project_contents.md"

# Clean up any existing output file
rm -f "$OUTPUT_FILE"

# Function to add a file header
add_file_header() {
    local filepath=$1
    echo -e "\n## File: $filepath\n" >> "$OUTPUT_FILE"
    echo '```' >> "$OUTPUT_FILE"
}

# Function to add a file footer
add_file_footer() {
    echo '```' >> "$OUTPUT_FILE"
}

# Create header for the document
echo "# Project Files Content" > "$OUTPUT_FILE"
echo "Generated on: $(date)" >> "$OUTPUT_FILE"

# Backend files
echo -e "\n# Backend Files" >> "$OUTPUT_FILE"

# Java files
BACKEND_FILES=(
    "src/main/java/com/allianz/raffle/RaffleApplication.java"
    "src/main/java/com/allianz/raffle/controller/RaffleController.java"
    "src/main/java/com/allianz/raffle/model/Raffle.java"
    "src/main/java/com/allianz/raffle/model/PrizeTier.java"
    "src/main/java/com/allianz/raffle/model/ApointsConfig.java"
    "src/main/java/com/allianz/raffle/model/enums/RaffleStatus.java"
    "src/main/java/com/allianz/raffle/service/RaffleService.java"
    "src/main/java/com/allianz/raffle/repository/RaffleRepository.java"
    "src/main/java/com/allianz/raffle/config/SecurityConfig.java"
    "src/main/java/com/allianz/raffle/config/OpenApiConfig.java"
)

# Configuration files
CONFIG_FILES=(
    "src/main/resources/application.properties"
    "src/main/resources/application-dev.properties"
    "src/main/resources/application-prod.properties"
    "pom.xml"
)

# Frontend files
echo -e "\n# Frontend Files" >> "$OUTPUT_FILE"

FRONTEND_FILES=(
    "src/app/components/raffle-list/raffle-list.component.ts"
    "src/app/components/raffle-detail/raffle-detail.component.ts"
    "src/app/components/admin/raffle-management/raffle-management.component.ts"
    "src/app/services/raffle.service.ts"
    "src/app/services/auth.service.ts"
    "src/app/models/raffle.model.ts"
    "src/app/guards/auth.guard.ts"
    "src/app/guards/admin.guard.ts"
    "src/app/interceptors/jwt.interceptor.ts"
    "src/app/interceptors/error.interceptor.ts"
    "src/app/app.module.ts"
    "src/app/app.component.ts"
    "src/app/app-routing.module.ts"
)

# Process backend files
echo "Processing backend files..."
for file in "${BACKEND_FILES[@]}"; do
    if [ -f "backend/$file" ]; then
        add_file_header "$file"
        cat "backend/$file" >> "$OUTPUT_FILE"
        add_file_footer
    else
        echo "Warning: File not found - backend/$file"
    fi
done

# Process config files
echo "Processing configuration files..."
for file in "${CONFIG_FILES[@]}"; do
    if [ -f "backend/$file" ]; then
        add_file_header "$file"
        cat "backend/$file" >> "$OUTPUT_FILE"
        add_file_footer
    else
        echo "Warning: File not found - backend/$file"
    fi
done

# Process frontend files
echo "Processing frontend files..."
for file in "${FRONTEND_FILES[@]}"; do
    if [ -f "frontend/$file" ]; then
        add_file_header "$file"
        cat "frontend/$file" >> "$OUTPUT_FILE"
        add_file_footer
    else
        echo "Warning: File not found - frontend/$file"
    fi
done

echo "Project files have been collected in $OUTPUT_FILE"
