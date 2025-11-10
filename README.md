# Java RESTful API Example (Without Spring Boot) - Maven Edition

This is a simple example of a RESTful API implemented in Java without using the Spring Boot framework. It uses only Java's built-in libraries and the Gson library for JSON processing. This version is configured as a Maven project for easier dependency management and build automation.

## Features

- Implements GET, POST, PUT, DELETE HTTP methods
- In-memory data storage (simulates a database)
- JSON request/response handling
- Custom request routing
- Basic error handling
- Maven configuration for build and dependency management

## Prerequisites

- Java 8 or higher
- Maven 3.6 or higher

## Project Structure

```
java-rest-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── restapi/
│   │   │               ├── Main.java          # Main server class
│   │   │               ├── model/
│   │   │               │   └── User.java     # User model class
│   │   │               ├── controller/
│   │   │               │   └── UserController.java  # User CRUD operations
│   │   │               └── util/
│   │   │                   ├── JSONUtils.java     # JSON serialization/deserialization
│   │   │                   └── RequestParser.java  # HTTP request parsing
│   │   └── resources/
│   │       └── (empty)
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── restapi/
│       │               └── controller/
│       │                   └── UserControllerTest.java  # Unit tests
│       └── resources/
│           └── (empty)
├── pom.xml               # Maven configuration
└── README.md             # Project documentation
```

## How to Build and Run

### Using Maven Commands

1. **Compile the project**

```bash
mvn compile
```

2. **Run the server**

```bash
mvn exec:java
```

3. **Package the project (create JAR file)**

```bash
mvn package
```

4. **Run tests**

```bash
mvn test
```

5. **Clean the project**

```bash
mvn clean
```

### Running the Executable JAR

After packaging the project, you can run the executable JAR file:

```bash
java -jar target/java-rest-api-1.0.0.jar
```

The server will start on port 8080.

## API Endpoints

### Get all users
- **URL**: `/api/users`
- **Method**: `GET`
- **Response**: 
  - Status: 200 OK
  - Body: Array of user objects

### Get user by ID
- **URL**: `/api/users/{id}`
- **Method**: `GET`
- **Response**: 
  - Status: 200 OK (if found)
  - Status: 404 Not Found (if not found)
  - Body: User object (if found)

### Create a new user
- **URL**: `/api/users`
- **Method**: `POST`
- **Headers**: `Content-Type: application/json`
- **Body**: 
  ```json
  {
    "name": "John Doe",
    "email": "john@example.com",
    "age": 30
  }
  ```
- **Response**: 
  - Status: 201 Created (if successful)
  - Status: 400 Bad Request (if invalid data)
  - Body: Created user object (with generated ID)

### Update an existing user
- **URL**: `/api/users/{id}`
- **Method**: `PUT`
- **Headers**: `Content-Type: application/json`
- **Body**: 
  ```json
  {
    "name": "Updated Name",
    "email": "updated@example.com",
    "age": 35
  }
  ```
- **Response**: 
  - Status: 200 OK (if successful)
  - Status: 404 Not Found (if user not found)
  - Body: Updated user object

### Delete a user
- **URL**: `/api/users/{id}`
- **Method**: `DELETE`
- **Response**: 
  - Status: 204 No Content (if successful)
  - Status: 404 Not Found (if user not found)

## Testing the API

You can test the API using tools like `curl`, Postman, or any other HTTP client.

### Example curl commands

1. **Get all users**
```bash
curl http://localhost:8080/api/users
```

2. **Get user by ID**
```bash
curl http://localhost:8080/api/users/1
```

3. **Create a new user**
```bash
curl -X POST -H "Content-Type: application/json" -d '{"name":"Jane Doe","email":"jane@example.com","age":28}' http://localhost:8080/api/users
```

4. **Update a user**
```bash
curl -X PUT -H "Content-Type: application/json" -d '{"name":"Jane Smith","email":"jane.smith@example.com","age":29}' http://localhost:8080/api/users/4
```

5. **Delete a user**
```bash
curl -X DELETE http://localhost:8080/api/users/4
```

## Notes

- The data is stored in memory, so it will be lost when the server is restarted.
- This is a simple example for educational purposes. In a production environment, you would want to add:
  - Persistent data storage
  - Authentication and authorization
  - Input validation
  - Logging
  - More robust error handling
  - Rate limiting
  - HTTPS support
