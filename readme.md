# ToDo App

This project is a ToDo application that uses Spring Boot and Couchbase to manage todo items. Follow the instructions below to build and run the application.

## Prerequisites

Before you begin, ensure you have the following installed:
- Java JDK 22
- Maven 3.8.4 or higher (compatible with JDK 22)
- Docker Desktop or Docker Engine

## Building the Application

To build the application and create an executable jar file, run the following command from the root directory of the project:

```bash
mvn clean install
```
## Running Tests

To run the test suite, including unit and integration tests, use the following Maven command:
```bash
mvn test
```

## Running the Application

To run the application with all dependencies like Couchbase, you can use Docker Compose. This setup requires a `docker-compose.yml` file configured with your services.

1. **Start Docker Compose**:
```bash
docker-compose up --build
```

## Accessing the Application
Once the application is running, you can access it via:

http://localhost:8080

For API documentation and interactive testing, access the Swagger UI at:

http://localhost:8080/swagger-ui.html



