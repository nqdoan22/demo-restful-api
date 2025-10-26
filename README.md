# REST API Implementation Report - API Logging System

## Project Overview
A Spring Boot RESTful API for managing `film` and `actor` in the Sakila database with comprehensive request/response logging system.

## Technology Stack
- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **MySQL Database**
- **Logback**
- **Maven**
- **Lombok**

## Implementation Details

### Step 1: Logback Configuration (`logback-spring.xml`)

Created Logback configuration with:
- Console appender for development
- File appender with daily rotation and size-based rotation
- Automatic compression of old logs
- Separate appender for API request logs

### Step 2: LogEntry Entity

**File:** `src/main/java/com/web/restapidemo/entity/LogEntry.java`

Entity to store logs in database:
- `id`: Primary key
- `timestamp`: Request time
- `method`: HTTP method
- `uri`: Request URI with query params
- `requestBody`: Request body content
- `responseStatus`: HTTP status code
- `responseBody`: Response body content
- `executionTimeMs`: Processing time
- `clientIp`: Client IP address
- `userAgent`: Browser/client info

### Step 3: LoggingInterceptor

**File:** `src/main/java/com/web/restapidemo/interceptor/LoggingInterceptor.java`

- Intercepts all HTTP requests/responses
- Records execution time
- Captures client IP and user agent
- Saves logs to both file and database

### Step 4: LogService

**File:** `src/main/java/com/web/restapidemo/service/LogService.java`

Service for log operations:
- Save logs to database
- Search logs by keyword
- Find logs by date range
- Find slow requests
- Filter by method or status code

### Step 5: LogController

**File:** `src/main/java/com/web/restapidemo/controller/LogController.java`

API endpoints for log searching:
- `GET /api/logs/search?keyword=...` - Search by keyword
- `GET /api/logs/date-range?startDate=...&endDate=...` - Filter by date
- `GET /api/logs/slow-requests?thresholdMs=...` - Find slow requests
- `GET /api/logs/method/{method}` - Filter by HTTP method
- `GET /api/logs/status/{status}` - Filter by status code

### Step 6: WebConfig

**File:** `src/main/java/com/web/restapidemo/config/WebConfig.java`

Registers LoggingInterceptor to capture all requests.

## Logging Features

### 1. Request/Response Logging
- Logs all API requests and responses
- Captures: method, URI, request/response body, status code, execution time
- Records client IP and user agent

### 2. Multiple Storage Options
- **File Logging:** Logs saved to text files with rotation
- **Database Logging:** Logs stored in `api_log` table

### 3. Log Rotation
- **Daily Rotation:** New log file each day
- **Size-based Rotation:** When file reaches 10MB
- **Automatic Compression:** Old logs compressed to .gz
- **Retention:** Keeps 30 days of logs
- **Total Size Cap:** Maximum 1GB for all logs

### 4. Log Search & Debugging
- Search logs by keyword
- Filter by date range
- Find slow requests (>threshold)
- Filter by HTTP method
- Filter by response status code

## How to Use Logging Features

### View Log Files
```bash
# Application logs
tail -f logs/api-logs.log

# API request logs
tail -f logs/api-requests.log

# View rotated logs
ls logs/
```

### Search Logs via API

#### Search by Keyword
```bash
curl "http://localhost:8080/api/logs/search?keyword=matrix"
```

#### Filter by Date Range
```bash
curl "http://localhost:8080/api/logs/date-range?startDate=2024-10-01T00:00:00&endDate=2024-10-31T23:59:59"
```

#### Find Slow Requests
```bash
curl "http://localhost:8080/api/logs/slow-requests?thresholdMs=1000"
```

#### Filter by Method
```bash
curl http://localhost:8080/api/logs/method/GET
```

#### Filter by Status Code
```bash
curl http://localhost:8080/api/logs/status/200
```

### Database Query Examples

```sql
-- View all logs
SELECT * FROM api_logs ORDER BY timestamp DESC;

-- Find errors
SELECT * FROM api_logs WHERE response_status >= 400;

-- Find slow requests
SELECT * FROM api_logs WHERE execution_time_ms > 1000;

-- Search logs
SELECT * FROM api_logs WHERE uri LIKE '%actor%';
```

## Log Rotation Strategy

The system implements multiple rotation strategies:

1. **Daily Rotation:** New log file each day (e.g., `api-logs-2024-10-24.0.log.gz`)
2. **Size-based Rotation:** When file reaches 10MB, a new index is created (e.g., `.1.log`, `.2.log`)
3. **Auto-compression:** Old logs automatically compressed to `.gz`
4. **Retention:** Keeps last 30 days of logs
5. **Total Size Cap:** Maximum 1GB total size for all log files

**Example log files:**
```
logs/
├── api-logs.log (current)
├── api-logs-2024-10-23.0.log.gz
├── api-logs-2024-10-23.1.log.gz
├── api-requests.log (current)
└── api-requests-2024-10-23.0.log.gz
```

## Testing the Logging System

1. **Make API requests:**
   ```bash
   curl http://localhost:8080/api/actors
   curl http://localhost:8080/api/films
   ```

2. **Check logs:**
   ```bash
   tail -f logs/api-logs.log
   tail -f logs/api-requests.log
   ```

3. **Search in database:**
    - Access MySQL: `mysql -u root -p sakila`
    - Query logs: `SELECT * FROM api_logs ORDER BY timestamp DESC LIMIT 10;`

4. **Use search APIs:**
   ```bash
   curl http://localhost:8080/api/logs/search?keyword=actor
   ```

## Project Structure

```
rest-api-demo/
├── src/main/java/com/web/restapidemo/
│   ├── config/
│   │   └── WebConfig.java           # Register interceptor
│   ├── controller/
│   │   └── LogController.java       # Log search API
│   ├── entity/
│   │   └── LogEntry.java            # Log entity
│   ├── interceptor/
│   │   └── LoggingInterceptor.java  # Capture requests
│   ├── repository/
│   │   └── LogEntryRepository.java  # Log queries
│   └── service/
│       └── LogService.java           # Log business logic
├── src/main/resources/
│   └── logback-spring.xml            # Log configuration
└── README.md
```

## Conclusion

Successfully implemented comprehensive logging system:
- Logs all API requests and responses
- Saves to both file and database
- Automatic log rotation (daily and size-based)
- Search and debugging capabilities
- Multiple filter options (keyword, date, method, status, performance)
