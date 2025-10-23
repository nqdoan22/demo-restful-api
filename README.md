# REST API Demo - Actor Management System

## Project Overview
A Spring Boot RESTful API for managing actor information with basic CRUD operations.

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

### Step 3: Entity Class (Actor.java)
- Created Actor class with JPA annotations
- Defined fields: id, firstName, lastName, lastUpdate
- Used Lombok for getters/setters
### Step 4: Repository Layer (ActorRepository.java)
- Created repository interface extending JpaRepository
- Used Spring Data JPA for automatic CRUD operations
### Step 5: Service Layer (ActorService.java)
- Implemented business logic for actor management
- Added methods for all CRUD operations
### Step 6: Controller Layer (ActorController.java)
- Created REST controller with API endpoints
- Handled HTTP methods: GET, POST, PUT, DELETE
- Fixed parameter binding issues during testing

## Features Completed:  
- View all actors
- View actor details by ID
- Add new actor
- Update existing actor
- Delete actor