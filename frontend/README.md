# Raffle System Frontend

Angular-based frontend for the Raffle System application.

## Prerequisites

1. Node.js v14 or later and npm
   ```bash
   # Install Node.js using Homebrew
   brew install node
   ```

## Running the Application

### Using the Shell Script (Recommended)

1. Make the script executable (only needed once):
   ```bash
   chmod +x run-frontend.sh
   ```

2. Run the application:
   ```bash
   # Run in development mode (default)
   ./run-frontend.sh

   # Run in production mode
   ./run-frontend.sh prod
   ```

The script will automatically:
- Check for Node.js and npm installation
- Install dependencies if needed
- Start the appropriate server

### Using npm Directly

1. Install dependencies:
   ```bash
   npm install
   ```

2. Run the application:
   ```bash
   # Development server
   npm run start:dev

   # Production server
   npm run start:prod
   ```

The application will be available at:
- Development: http://localhost:4200
- Production: http://localhost:8080 (when using serve:dist)

## Available Scripts

- `npm run start:dev` - Starts the development server
- `npm run start:prod` - Starts the production server
- `npm run build:dev` - Creates a development build
- `npm run build:prod` - Creates a production build
- `npm run test` - Runs the unit tests
- `npm run test:ci` - Runs the unit tests in CI mode
- `npm run lint` - Lints the source code
- `npm run clean` - Cleans the build directory
- `npm run serve:dist` - Serves the production build

## Project Structure

```
frontend/
├── src/
│   ├── app/
│   │   ├── components/         # UI components
│   │   │   ├── raffle-list/
│   │   │   ├── raffle-detail/
│   │   │   └── admin/
│   │   ├── services/          # Angular services
│   │   ├── models/            # TypeScript interfaces
│   │   ├── guards/            # Route guards
│   │   └── interceptors/      # HTTP interceptors
│   ├── assets/                # Static files
│   └── environments/          # Environment configurations
├── angular.json               # Angular workspace config
├── package.json              # Project dependencies
├── tsconfig.json            # TypeScript config
└── run-frontend.sh          # Run script
```

## UI Components

The frontend uses the Allianz ng-aquila component library (@aposin/ng-aquila) for consistent styling and UX.

## Testing

```bash
# Run tests
npm test

# Run tests in CI mode with coverage
npm run test:ci
```

## Building for Production

1. Build the application:
   ```bash
   npm run build:prod
   ```

2. The build artifacts will be stored in the `dist/raffle-system-frontend` directory.

3. Serve the production build:
   ```bash
   npm run serve:dist
   ```

## Development Notes

- The application uses Angular 14
- Styling is handled by ng-aquila (Allianz Design System)
- TypeScript strict mode is enabled
- HTTP interceptors handle JWT authentication
- Routing is configured with lazy loading