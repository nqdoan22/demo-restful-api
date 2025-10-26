# REST API Implementation Report - Actor Management System

## Project Overview
A Spring Boot RESTful API for managing `actor` table in the `sakila` database with basic CRUD operations.

## Technology Stack
- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **MySQL Database**
- **Maven**
- **Lombok**

## Project Structure
```text
rest-api-demo/
├── src/
│   └── main/
│       └── java/
│           └── com/web/rest_api_demo/
│               ├── entity/
│               │   └── Actor.java
│               ├── repository/
│               │   └── ActorRepository.java
│               ├── service/
│               │   └── ActorService.java
│               ├── controller/
│               │   └── ActorController.java
│               └── RestApiDemoApplication.java
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

## Development Process

### Step 1: Project Initialization
Used Spring Initializr with:
- Maven Project, Java 21, Spring Boot 3.5.6
- Dependencies: Spring Web, Spring Data JPA, MySQL Driver, Lombok

### Step 2: Database Configuration
**application.properties:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sakila?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Step 3: Entity Layer - Actor Model (Actor.java)
The Actor entity maps to the `actor` table in Sakila database:
- **Fields:** `id`, `firstName`, `lastName`, `lastUpdate`
- **Annotations:** Uses JPA for database mapping
- **Utilities:** Lombok reduces boilerplate code (@Data, @Builder)

### Step 4: Repository Layer - Data Access (ActorRepository.java)
```java
public interface ActorRepository extends JpaRepository<Actor, Integer> { }
```
- Extends `JpaRepository` for automatic CRUD operations
- No custom queries needed for basic operations

### Step 5: Service Layer - Business Logic (ActorService.java)
**Methods implemented:**
- `getAllActors()` - Retrieve all actors
- `getActorById(id)` - Find actor by ID
- `createActor(actor)` - Save new actor
- `deleteActorById(id)` - Remove actor
- `updateActor(id, actor)` - Update existing actor

### Step 6: Controller Layer (ActorController.java)
API Endpoints:
#### 1. View All Actors
- **Method:** GET
- **Endpoint:** `/api/actors`
- **Response:** List of all actors

#### 2. View Actor Detail
- **Method:** GET
- **Endpoint:** `/api/actors/{id}`
- **Response:** Actor object or 404 if not found

#### 3. Add New Actor
- **Method:** POST
- **Endpoint:** `/api/actors`
- **Request Body:** Actor JSON object
- **Response:** Created actor with 201 status

**Example Request:**
```json
{
  "firstName": "JOHN",
  "lastName": "DOE"
}
```

#### 4. Update Actor
- **Method:** PUT
- **Endpoint:** `/api/actors/{id}`
- **Request Body:** Actor JSON object
- **Response:** Updated actor or 404 if not found

#### 5. Delete Actor
- **Method:** DELETE
- **Endpoint:** `/api/actors/{id}`
- **Response:** 204 No Content




## Conclusion

Successfully implemented all required CRUD operations for managing actors:

- View list of all actors
- View actor details
- Add new actor
- Delete actor
- Update actor

The API follows REST principles and uses appropriate HTTP methods and status codes.