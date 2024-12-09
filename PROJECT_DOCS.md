# Raffle System Project Documentation

## Project Overview
The Raffle System is a web application that allows users to participate in raffles by answering questions and optionally purchasing additional chances with virtual currency (APoints). It consists of a Spring Boot backend and an Angular frontend.

## Backend (Java/Spring Boot)
--------------------

### Key Technologies
- Spring Boot 2.7.0
- Spring Security with JWT
- SQLite Database
- OpenAPI/Swagger Documentation
- Maven for dependency management

### Directory Structure
```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/allianz/raffle/
│   │   │   ├── RaffleApplication.java            # Main application entry point
│   │   │   ├── controller/
│   │   │   │   └── RaffleController.java         # REST endpoints for raffle operations
│   │   │   ├── model/
│   │   │   │   ├── Raffle.java                  # Main raffle entity
│   │   │   │   ├── PrizeTier.java               # Prize configuration entity
│   │   │   │   ├── ApointsConfig.java           # Virtual currency configuration
│   │   │   │   └── enums/
│   │   │   │       └── RaffleStatus.java        # Raffle state enumeration
│   │   │   ├── service/
│   │   │   │   └── RaffleService.java           # Business logic for raffles
│   │   │   ├── repository/
│   │   │   │   └── RaffleRepository.java        # Data access layer
│   │   │   ├── exception/
│   │   │   │   └── ValidationException.java      # Custom validation exception
│   │   │   └── config/
│   │   │       ├── SecurityConfig.java          # JWT and security configuration
│   │   │       └── OpenApiConfig.java           # Swagger documentation config
│   │   └── resources/
│   │       ├── application.properties           # Common application properties
│   │       ├── application-dev.properties       # Development configuration
│   │       └── application-prod.properties      # Production configuration
│   └── test/
└── pom.xml                                      # Maven dependencies and build config

### Services
1. RaffleService
   - createRaffle(Raffle) - Creates a new raffle with validation
   - getAllRaffles() - Gets all raffles with automatic status updates
   - getActiveRaffles() - Gets raffles in ACTIVE status
   - getPreviewRaffles() - Gets raffles in PREVIEW status
   - conductDraw(Long) - Conducts raffle draw (to be implemented)

### Validation Rules
1. Raffle Creation
   - Name is required and cannot be empty
   - Preview date must be before start date
   - Start date must be before end date
   - Question is required and cannot be empty
   - Minimum of 2 answer options required
   - At least one prize tier required
   - Prize tier validation:
     - Valid tier number (>= 0)
     - Non-empty description
     - Quantity >= 1

2. Status Management
   - DRAFT: Initial status for new raffles
   - PREVIEW: After preview date, before start date
   - ACTIVE: Between start and end date
   - ENDED: After end date
   - DRAWN: After winners selected

### API Endpoints
Base URL: http://localhost:8080/api

#### Raffle Endpoints
1. GET /raffles
   - Optional query parameter: status (active/preview)
   - Returns list of raffles with filtered status
   - Response: 200 OK with raffle list

2. POST /raffles
   - Creates new raffle
   - Request body: Raffle JSON
   - Response: 201 CREATED with created raffle
   - Error: 400 BAD REQUEST with validation errors

Example Request Body:
```json
{
  "name": "Sample Raffle",
  "description": "Win awesome prizes!",
  "teaserImage": "https://example.com/teaser.jpg",
  "detailImage": "https://example.com/detail.jpg",
  "startDate": "2024-12-10T10:00:00",
  "endDate": "2024-12-12T10:00:00",
  "previewDate": "2024-12-09T10:00:00",
  "question": "What's your favorite color?",
  "answerOptions": ["Red", "Blue", "Green"],
  "prizeTiers": [
    {
      "tier": 1,
      "description": "First Prize",
      "quantity": 1
    }
  ],
  "apointsConfig": {
    "costPerChance": 100,
    "maxPurchases": 5
  }
}
```

### Database Schema
1. Raffles Table
   - Primary key: id (Long)
   - Basic info: name, description, teaserImage, detailImage
   - Dates: previewDate, startDate, endDate
   - Question and answer_options (element collection)
   - Status enum
   - Embedded apoints_config

2. Prize_Tiers Table
   - Primary key: id (Long)
   - Foreign key: raffle_id
   - Fields: tier, description, quantity

### Authentication
- JWT-based authentication
- Token expiration: 24 hours
- Token prefix: "Bearer "

### Database
- Type: SQLite
- Development DB: raffles-dev.db
- Production DB: raffles-prod.db

### Development Credentials
```properties
# JWT Configuration
jwt.secret=dev-secret-key-should-be-replaced-in-production
jwt.expiration=86400000

# Email (Development)
spring.mail.host=localhost
spring.mail.port=25
spring.mail.username=test
spring.mail.password=test
```

## Frontend (Angular)
--------------------

[Rest of the frontend documentation remains unchanged]

## Development Setup Instructions
1. Backend:
   ```bash
   cd backend
   ./run-backend.sh dev
   ```

2. Frontend:
   ```bash
   cd frontend
   ./run-frontend.sh dev
   ```

## Access URLs
- Frontend: http://localhost:4200
- Backend API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs
