# REST API Security Implementation Report

## Project Overview
Implementation of API Key Authentication system for Spring Boot RESTful API to protect backend endpoints and identify clients for billing and authorization purposes.

## Requirements
1. Protect system from unauthorized access
2. Identify clients (internal and external - under 10 external clients) for billing and access control
3. Solution complexity appropriate for the use case

## Technology Stack
- Java 21
- Spring Boot 3.5.6
- Spring Data JPA
- MySQL Database (Sakila database)
- Lombok
- Maven

## Implementation Details

### Step 1: ApiClient Entity

**File:** `src/main/java/com/web/restapidemo/entity/ApiClient.java`

Entity to store API client information with the following fields:
- `id`: Primary key
- `clientName`: Unique client name
- `apiKey`: Unique 32-character API key
- `description`: Client description
- `status`: ACTIVE or INACTIVE status
- `createdAt`: Account creation timestamp
- `lastUsedAt`: Last API usage timestamp
- `requestCount`: Total number of requests
- `contactEmail`: Contact email address
- `clientType`: INTERNAL or EXTERNAL classification

### Step 2: ApiClientRepository

**File:** `src/main/java/com/web/restapidemo/repository/ApiClientRepository.java`

Spring Data JPA repository interface extending JpaRepository with custom query methods:
- `findByApiKey()`: Find client by API key
- `findByClientName()`: Find client by name
- `findByStatus()`: Find clients by status
- `findByClientType()`: Find clients by type

### Step 3: ApiClientService

**File:** `src/main/java/com/web/restapidemo/service/ApiClientService.java`

Service layer providing:
- API key generation using SecureRandom (32 characters)
- API key validation with automatic usage tracking
- Client CRUD operations
- API key rotation for security
- Client status management (ACTIVE/INACTIVE)

Key method: `validateApiKey()` - Validates API key, checks client status, updates usage statistics automatically.

### Step 4: ApiKeyInterceptor

**File:** `src/main/java/com/web/restapidemo/interceptor/ApiKeyInterceptor.java`

Custom interceptor that:
- Intercepts all HTTP requests before controllers
- Extracts X-API-Key header from requests
- Validates API key against database
- Checks if client is ACTIVE
- Returns 401 Unauthorized if invalid or missing
- Skips validation for admin endpoints

Error responses include:
- Missing API key: Returns 401 with error message
- Invalid API key: Returns 401 with error message
- Inactive client: Returns 401 with error message

### Step 5: AdminController

**File:** `src/main/java/com/web/restapidemo/controller/AdminController.java`

Admin API for managing clients (NO AUTHENTICATION REQUIRED):

Endpoints:
- GET /api/admin/clients - Get all clients
- GET /api/admin/clients/{id} - Get client by ID
- POST /api/admin/clients - Create new client (auto-generates API key)
- PUT /api/admin/clients/{id} - Update client
- DELETE /api/admin/clients/{id} - Delete client
- GET /api/admin/clients/active - Get active clients only
- GET /api/admin/clients/type/{type} - Get clients by type
- POST /api/admin/clients/{id}/rotate-key - Rotate API key

### Step 6: WebConfig Update

**File:** `src/main/java/com/web/restapidemo/config/WebConfig.java`

Updated to register ApiKeyInterceptor:
- Runs before LoggingInterceptor
- Applies to all /api/** endpoints
- Excludes /api/admin/** endpoints
- Excludes /swagger-ui/** and /api-docs/**

### Step 7: LoggingInterceptor Update

**File:** `src/main/java/com/web/restapidemo/interceptor/LoggingInterceptor.java`

Enhanced to log client information:
- Extracts client name from request attribute
- Logs client name in file logs
- Saves partial API key in database (first 8 chars for security)
- Tracks which client made each request

## Security Features

### 1. API Key Authentication
- 32-character random keys generated using SecureRandom
- Case-sensitive validation
- Stored in database with unique constraint
- Supports key rotation for security

### 2. Client Status Management
- Clients can be ACTIVE or INACTIVE
- Inactive clients cannot access APIs
- Status managed via Admin API

### 3. Usage Tracking
- Automatic request count tracking per client
- Last used timestamp tracking
- Useful for billing and monitoring

### 4. Client Classification
- INTERNAL: Company employees, mobile apps, admin systems
- EXTERNAL: Partner companies, demo clients, external users (under 10)
- Supports filtering by type

### 5. Protected Endpoints
- All /api/** endpoints require valid API key
- Admin endpoints excluded for management
- Swagger UI excluded for documentation

### 6. Request Logging
- All requests logged with client name
- API key partially logged (first 8 chars)
- Timestamp, method, URI, status, execution time tracked

## How to Use

### Create New Client

```
POST http://localhost:8080/api/admin/clients

{
  "clientName": "My Mobile App",
  "description": "Mobile application for internal use",
  "contactEmail": "mobile@company.com",
  "clientType": "INTERNAL"
}

Response includes auto-generated API key
```

### Use API with API Key

```
GET http://localhost:8080/api/actors
Headers:
  X-API-Key: <your-api-key>
```

### Manage Clients

```
GET /api/admin/clients - List all clients
GET /api/admin/clients/active - Active clients only
POST /api/admin/clients/{id}/rotate-key - Rotate API key
PUT /api/admin/clients/{id} - Update client
DELETE /api/admin/clients/{id} - Delete client
```

## Testing

### Test Cases

1. Request without API key - Returns 401 Unauthorized
2. Request with valid API key - Returns 200 OK
3. Request with invalid API key - Returns 401 Unauthorized
4. Request with inactive client API key - Returns 401 Unauthorized
5. Admin endpoints accessible without API key
6. Request count increases after successful requests
7. Last used timestamp updates automatically
8. Client name appears in logs

### Example Test Commands

```
# Without API key (FAIL)
curl http://localhost:8080/api/actors

# With valid API key (SUCCESS)
curl -H "X-API-Key: <api-key>" http://localhost:8080/api/actors
```

## Project Structure

```
src/main/java/com/web/restapidemo/
├── config/
│   └── WebConfig.java                    # Register interceptors
├── controller/
│   └── AdminController.java              # Client management API
├── entity/
│   └── ApiClient.java                    # Client entity
├── interceptor/
│   ├── ApiKeyInterceptor.java            # API key validation
│   └── LoggingInterceptor.java           # Enhanced logging
├── repository/
│   └── ApiClientRepository.java          # Client data access
└── service/
    └── ApiClientService.java             # Client business logic
```

## Solution Architecture

Request Flow:
1. Client sends HTTP request with X-API-Key header
2. ApiKeyInterceptor validates API key
3. If valid and client is ACTIVE, request proceeds to controller
4. Controller processes business logic
5. LoggingInterceptor logs request with client name
6. Response returned to client

Security Layers:
1. Transport Layer: HTTPS recommended
2. Authentication Layer: API key validation
3. Authorization Layer: Client status check
4. Audit Layer: Request logging with client identification
5. Management Layer: Admin API for client lifecycle

## Benefits

For Internal Clients:
- Simple authentication mechanism
- Easy integration with applications
- Centralized client management
- Usage tracking for monitoring

For External Clients (under 10):
- Clear identification for billing
- Access control via status
- Usage statistics for cost analysis
- API key rotation for security

For System Administrators:
- Complete client visibility
- Request tracking and auditing
- Easy client onboarding/offboarding
- Security with key rotation

## Conclusion

Successfully implemented API Key Authentication system with:
- Client authentication for all requests
- Client management via Admin API
- Usage tracking for billing and monitoring
- Client identification for internal and external (< 10) clients
- Request logging with client information
- Security features including key rotation

The solution provides appropriate security for internal clients and client identification for external partners without unnecessary complexity.

