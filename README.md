# Secure Auth JWT with PostgreSQL & Redis  
This is a hands-on learning project built with Spring Boot to practice secure user authentication using JWT tokens, PostgreSQL, and Redis. It demonstrates how to manage user signup, login, logout, and token blacklisting — all while applying real-world security best practices.

---
## Features  
- JWT-based authentication system
- Secure user registration (/v1/auth/signUp)
- User login with token generation (/v1/auth/signIn)
- Logout with token blacklisting (/v1/auth/signOut)
- Exception handling with meaningful HTTP error responses
- Global request validation
- Swagger API documentation for easy testing---

## Tech Stack & Dependencies

- **Java 23**
- **Spring Boot 3.5.0**
- **Maven** – for project build and dependency management
- **PostgreSQL** – for persistent user and token storage
- **Redis** – to store blacklisted tokens
- **JJWT** – JSON Web Token library for signing and validating tokens

### Dependencies Overview

| Dependency                                 | Purpose / Description                                                                 |
|--------------------------------------------|----------------------------------------------------------------------------------------|
| `spring-boot-starter-web`                  | For REST API development|
| `spring-boot-starter-security`             | Core Spring Security for request authentication                  |
| `jjwt-apijjwt-impljjwt-jackson`            | JWT creation and validation|
| `spring-boot-starter-data-jpa`             | JPA for interacting with PostgreSQL                 |
| `postgresql`                               | PostgreSQL JDBC driver                 |
| `spring-boot-starter-data-redis	`          | Redis access and configuration               |
| `lombok`                                   | Reduces boilerplate code         |
| `springdoc-openapi-starter-webmvc-ui`      | Auto-generates Swagger UI documentation                  |

---

## Swagger UI

The API documentation is automatically generated and accessible at:  
```[ http://localhost:8181/swagger-ui.html](http://localhost:8080/swagger-ui/index.html ```  

---
## Project Structure

```text
secure-auth-jwt-postgres-redis/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── secureauth/
│                   ├── config/
│                   ├── controller/
│                   ├── dto/
│                   ├── entity/
│                   ├── exception/
│                   ├── repository/
│                   ├── security/
│                   └── service/
├── src/main/resources/
│   └── application.yml
├── pom.xml
└── README.md
```
## Running the Application
1- Make sure both PostgreSQL and Redis services are running: 
- **PostgreSQL:** localhost:5432, database: secureauth
- **Redis:** localhost:6379

2- Build and run the project:
```./mvnw spring-boot:run ```  

---
## Test Workflow

## Sign Up a user:
``` POST /v1/auth/signUp ```  
- Body:
```
{
  "username": "testuser",
  "password": "Password123"
}
```
## Sign In:
```POST /v1/auth/signIn```  
- Returns a JWT token if credentials are valid.

## Sign Out:
```POST /v1/auth/signOut```  
-  Header, Authorization: Bearer <your_token_here>

Once signed out, the token is stored in Redis as blacklisted.

## Token Reuse Attempt:
After signOut, using the same token again will result in a 401 Unauthorized.

## License
This project is licensed under the MIT License.

---
---
## Running Redis in Docker Desktop (for development)

1- Pull Redis image from Docker Hub:  
``` docker pull redis ```

2- Run Redis container:  
``` docker run --name secure-redis -p 6379:6379 -d redis ```

> This will: > - Run Redis in the background (-d) > - Map container port 6379 to your local 6379 > - Name the container secure-redis for easy reference

3- (Optional) Check if Redis is running:  
``` docker ps ```

> Example output: > 9f639178fbe9 redis "docker-entrypoint.s…" ... Up ... 0.0.0.0:6379->6379/tcp secure-redis >

4- (Optional) Connect to Redis CLI inside the container:  
``` docker exec -it secure-redis redis-cli ```

> Example response: > PONG

Redis is now running and ready at:  
```localhost:6379```

---
## Project Redis Configuration
- In your application.yml, make sure Redis connection settings are included:
```
spring:
  data:
    redis:
      host: localhost
      port: 6379
      timeout: 60000
```
- Make sure this dependency is included in your pom.xml:
```
 <!-- Redis: Provides Redis support for Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
```
This enables Spring Boot to connect with Redis using RedisTemplate and manage data operations like token blacklisting.
