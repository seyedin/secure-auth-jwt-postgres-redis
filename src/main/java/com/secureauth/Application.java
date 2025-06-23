package com.secureauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		log.info("\n Swagger OpenAPI documentation is available at: http://localhost:8080/swagger-ui.html");
		log.info("✅ SecureAuth app started successfully!");

		log.info("📖 API Documentation is available at: http://localhost:8080/swagger-ui/index.html");

		log.info("""
--------------------------------------------------------
✅ SecureAuth test app started successfully!
--------------------------------------------------------
   Requirements:
  🔸 PostgreSQL must be running at: localhost:5432 (DB: secureauth)
  🔸 Redis must be running at: localhost:6379
--------------------------------------------------------
📖 Swagger UI:
→ http://localhost:8080/swagger-ui/index.html
--------------------------------------------------------
How to test the app:

1️⃣ SIGN UP
POST /v1/auth/signUp
Body:
{
  "username": "testuser",
  "password": "Password123"
}

2️⃣ SIGN IN
POST /v1/auth/signIn
Use same body. Response will return a JWT token.

3️⃣ SIGN OUT
POST /v1/auth/signOut
Header:
Authorization: Bearer <token>

🛑 After signOut, the token is BLACKLISTED.
Trying to reuse the same token will return 401 Unauthorized!

🧪 This is a learning project using Spring Boot + JWT + Redis + Postgres.
It supports signUp, signIn, signOut, and blacklisting tokens.
--------------------------------------------------------
""");
	}
}

