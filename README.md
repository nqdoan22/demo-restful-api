# REST API Implementation Report - Film Management System

## Project Overview

This REST API Demo project manages the `film` table in the `sakila` database with validation and api documentation

## Technology Stack
- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **MySQL Database**
- **Jakarta Bean Validation**
- **OpenAPI 3 + Swagger UI**
- **Maven**
- **Lombok**

## Development Process

### 1. Add dependencies

**Configuration file (`pom.xml`):**
```xml
<!--        For validation -->
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!--        For api documentation-->
<dependency>
<groupId>org.springdoc</groupId>
<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
<version>2.8.8</version>
</dependency>
```

### 2. Key Entity Fields (from database):
- `filmId`: Unique identifier
- `title`: Film title (required, max 255 chars)
- `releaseYear`: Year (1901-2155)
- `rating`: Rating (G, PG, PG-13, R, NC-17)
- `rentalRate`: Rental price
- `replacementCost`: Replacement cost

### 3. Validation Client Parameters

#### 3.1 Number of Fields

All required fields are validated using Jakarta Bean Validation annotations:
- **Required fields:** `title`, `languageId`, `rentalDuration`, `rentalRate`, `replacementCost`
- **Optional fields:** `description`, `releaseYear`, `length`, `rating`

#### 3.2 DataType Validation

Accurate mapping between Java types and MySQL types is maintained:

| Java Type | MySQL Type |
|-----------|------------|
| Integer | SMALLINT, YEAR, TINYINT |
| String | VARCHAR(255), TEXT |
| BigDecimal | DECIMAL(4,2) |
| LocalDateTime | DATETIME |

#### 3.3 Field Values Validation

**Examples of validation rules:**

```java
// Title validation
@NotBlank(message = "Title is mandatory")
@Size(max = 255, message = "Title must not exceed 255 characters")
private String title;

// Release year validation
@Min(value = 1901, message = "Release year must be at least 1901")
@Max(value = 2155, message = "Release year must not exceed 2155")
private Integer releaseYear;

// Rating validation
@Pattern(regexp = "^(G|PG|PG-13|R|NC-17)?$", message = "Rating must be one of: G, PG, PG-13, R, NC-17")
private String rating;

// Rental rate validation
@NotNull(message = "Rental rate is mandatory")
@DecimalMin(value = "0.0", inclusive = false, message = "Rental rate must be greater than 0")
@Digits(integer = 2, fraction = 2, message = "Rental rate must have format nn.nn")
private BigDecimal rentalRate;
```

#### 3.4 Error Handling

Global exception handler catches validation errors and returns detailed messages:

**Example Error Response:**
```json
{
  "title": "Title is mandatory",
  "rating": "Rating must be one of: G, PG, PG-13, R, NC-17"
}
```

### 4. OpenAPI 3 + Swagger UI

#### Configuration
- Title: Sakila Film Management API
- Version: 1.0
- Swagger UI URL: `http://localhost:8080/swagger-ui.html`
- API Docs URL: `http://localhost:8080/api-docs`

#### Features

1. **Interactive API Testing:** Test all APIs directly in browser (similar to Postman)
2. **API Descriptions:** Each endpoint has detailed descriptions with examples
3. **Parameter Documentation:** All parameters documented with descriptions and examples
4. **Sample Data:** Pre-filled example data for easy testing
5. **Schema Documentation:** Complete data model documentation

#### How to Use Swagger UI

1. Start the application
2. Navigate to `http://localhost:8080/swagger-ui.html`
3. Expand any API endpoint
4. Click "Try it out" button
5. Fill parameters or use example data
6. Click "Execute" to test
7. View response with status code and data

### 5. RESTful API Documentation (10+ APIs)

#### Basic CRUD Operations:
1. **GET** `/api/films` - Get all films
2. **GET** `/api/films/{id}` - Get film by ID
3. **POST** `/api/films` - Create new film
4. **PUT** `/api/films/{id}` - Update film
5. **DELETE** `/api/films/{id}` - Delete film

#### Search & Filter Operations:
6. **GET** `/api/films/search?title={title}` - Search by title
7. **GET** `/api/films/rating/{rating}` - Filter by rating
8. **GET** `/api/films/year/{year}` - Filter by release year
9. **GET** `/api/films/rental-range?minRate={min}&maxRate={max}` - Filter by price range
10. **GET** `/api/films/long-films?minLength={length}` - Filter by film length

## Sample Request & Response

**Create Film Request:**
```json
{
    "title": "The Matrix",
    "description": "A computer hacker learns about reality",
    "releaseYear": 1999,
    "languageId": 1,
    "rentalDuration": 3,
    "rentalRate": 4.99,
    "length": 136,
    "replacementCost": 19.99,
    "rating": "R"
}
```

**Response:**
```json
{
    "filmId": 1,
    "title": "The Matrix",
    "releaseYear": 1999,
    "rentalRate": 4.99,
    "rating": "R",
    ...
}
```


## Key Features
 
- **Validation:** Complete client parameter validation (number, datatype, field values)  
- **10+ APIs:** Full CRUD operations plus search/filter capabilities  
- **API Documentation:** Complete OpenAPI 3 specification with Swagger UI  
- **Interactive Testing:** Built-in UI for testing APIs (similar to Postman)  
- **Error Handling:** Global exception handler with detailed error messages  
- **Sample Data:** Comprehensive examples for request and response

## Conclusion

The project successfully meets all requirements:
- Database setup with Sakila films table
- Validation of client parameters (field count, datatype, field values)
- RESTful API documentation with 10+ endpoints
- OpenAPI 3 with Swagger UI for interactive testing
- Sample data provided for all APIs

---

## How to Run

```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run

# Access Swagger UI
http://localhost:8080/swagger-ui.html
```
