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
   - createRaffle(Raffle)
   - getRaffleById(Long)
   - getAllRaffles()
   - conductDraw(Long)

### API Endpoints
Base URL: http://localhost:8080/api

#### Raffle Endpoints
- GET /raffles - Get all raffles
- GET /raffles/{id} - Get raffle by ID
- POST /raffles - Create new raffle
- PUT /raffles/{id} - Update raffle
- POST /raffles/{id}/draw - Conduct raffle draw

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

### Key Technologies
- Angular 14
- ng-aquila UI library
- Angular Reactive Forms
- JWT Authentication
- RxJS for async operations

### Directory Structure
```
frontend/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   ├── raffle-list/                    # Main raffle listing page
│   │   │   ├── raffle-detail/                  # Single raffle view
│   │   │   └── admin/
│   │   │       └── raffle-management/          # Admin raffle management
│   │   ├── services/
│   │   │   ├── raffle.service.ts              # API communication for raffles
│   │   │   └── auth.service.ts                # Authentication handling
│   │   ├── models/
│   │   │   └── raffle.model.ts               # TypeScript interfaces
│   │   ├── guards/
│   │   │   ├── auth.guard.ts                 # Route protection
│   │   │   └── admin.guard.ts                # Admin route protection
│   │   ├── interceptors/
│   │   │   ├── jwt.interceptor.ts            # JWT header injection
│   │   │   └── error.interceptor.ts          # Global error handling
│   │   ├── app.module.ts                     # Main application module
│   │   ├── app.component.ts                  # Root component
│   │   └── app-routing.module.ts             # Route configuration
│   ├── assets/                               # Static files
│   ├── environments/                         # Environment configurations
│   └── styles.scss                          # Global styles
├── angular.json                              # Angular workspace config
└── package.json                              # Node dependencies

### Components
1. RaffleListComponent
   - Displays grid of available raffles
   - Allows filtering and sorting
   - Links to raffle details

2. RaffleDetailComponent
   - Shows full raffle information
   - Handles question answering
   - Manages APoints purchases

3. RaffleManagementComponent
   - CRUD operations for raffles
   - Draw management
   - Prize configuration

### Services
1. RaffleService
   - getAllRaffles()
   - getRaffleById(id)
   - createRaffle(raffle)
   - updateRaffle(id, raffle)
   - conductDraw(id)

2. AuthService
   - login(username, password)
   - logout()
   - getCurrentUser()
   - isAuthenticated()

### Routes
- / → Redirect to /raffles
- /raffles → RaffleListComponent
- /raffles/:id → RaffleDetailComponent
- /admin/raffles → RaffleManagementComponent (protected)

### Development Server
- URL: http://localhost:4200
- API Proxy: http://localhost:8080/api

### ng-aquila Components Used
- NxButtonModule
- NxCardModule
- NxInputModule
- NxMessageModule
- NxSpinnerModule
- NxFormfieldModule
- NxGridModule
- NxHeadlineModule
- NxTableModule
- NxHeaderModule
- NxRadioModule

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
