# Raffle System Backend

Spring Boot backend for the Raffle System application.

## Prerequisites

1. Java Development Kit (JDK) 11 or later
   ```bash
   # Install OpenJDK 11 using Homebrew
   brew install openjdk@11
   
   # Create a symbolic link to make the JDK available system-wide
   sudo ln -sfn $(brew --prefix)/opt/openjdk@11/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-11.jdk
   ```

2. Maven 3.6 or later
   ```bash
   # Install Maven using Homebrew
   brew install maven
   ```

3. SQLite 3 (included with project dependencies)

## Running the Application

### Using the Shell Script (Recommended)

1. Make the script executable (only needed once):
   ```bash
   chmod +x run-backend.sh
   ```

2. Run the application:
   ```bash
   # Run in development mode (default)
   ./run-backend.sh

   # Run in production mode
   ./run-backend.sh prod
   ```

### Using Maven Directly

1. Build the application:
   ```bash
   mvn clean install
   ```

2. Run the application:
   ```bash
   # Run in development mode
   java -jar -Dspring.profiles.active=dev target/raffle-system-1.0-SNAPSHOT.jar

   # Run in production mode
   java -jar -Dspring.profiles.active=prod target/raffle-system-1.0-SNAPSHOT.jar
   ```

## API Documentation

Once the application is running, you can access the Swagger UI at:
- Development: http://localhost:8080/swagger-ui.html
- Production: https://your-domain/swagger-ui.html

The raw OpenAPI specification is available at:
- Development: http://localhost:8080/v3/api-docs
- Production: https://your-domain/v3/api-docs

## Environment Variables for Production

The following environment variables need to be set when running in production mode:

```bash
# JWT Configuration
export JWT_SECRET=your-secret-key

# Email Configuration
export SMTP_HOST=smtp.your-provider.com
export SMTP_PORT=587
export SMTP_USERNAME=your-username
export SMTP_PASSWORD=your-password

# CORS Configuration
export CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com

# SSL Configuration
export SSL_KEYSTORE_PATH=/path/to/keystore.p12
export SSL_KEYSTORE_PASSWORD=your-keystore-password
```

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── allianz/
│   │   │           └── raffle/
│   │   │               ├── controller/    # REST endpoints
│   │   │               ├── service/       # Business logic
│   │   │               ├── repository/    # Data access
│   │   │               ├── model/         # Domain models
│   │   │               └── config/        # Configurations
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
├── pom.xml
└── run-backend.sh
```

## Database

The application uses SQLite as the database. The database files are created automatically:
- Development: `raffles-dev.db`
- Production: `raffles-prod.db`

## Testing

```bash
# Run tests
mvn test

# Run tests with coverage report
mvn test jacoco:report
```

The coverage report will be available in `target/site/jacoco/index.html`