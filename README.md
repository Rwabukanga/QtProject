

User Registration & Login
Registration Endpoint
URL: /api/auth/register
Method: POST
Request Body:
json
Copy
Edit
{
    "username":"fils",
    "email":"sethfils4@gmail.com",
    "password":"Seth@123",
    "roles":"ROLE_ADMIN"
}
Response
{
    "id": 1,
    "username": "fils",
    "email": "sethfils4@gmail.com",
    "password": "$2a$10$wLaA6Kpfqfchr.4MJsZSYuOJ1rlNzQRTzZHvDuuKEIPs7UqSrb/uO",
    "roles": [],
    "reg": {
        "id": 1,
        "username": "fils",
        "email": "sethfils4@gmail.com",
        "password": "Seth@123",
        "roles": []
    }
}
Login Endpoint
URL: /api/auth/login
Method: POST
Request Body:
{
    "username":"Jean",
    "password":"jean@123"
}

Response:
{
    "id": 2,
    "username": "Jean",
    "email": "jean4@gmail.com",
    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJKZWFuIiwiaWF0IjoxNzQxODczNTY1LCJleHAiOjE3NDE5NTk5NjV9.RqomcSCICJFT8Ff-vYmPpCa3r7HvYohjSYNoToKmChDYsySE3nADITNaCFJKaZRUSSoWRATWY0xv5vBNSC1EUA",
    "tokenType": "Bearer"
}


Authentication Flow:
User Registration:

User provides a username, email, and password.
The password is hashed using bcrypt or Argon2 before being stored in the database.
Upon successful registration, the user receives a confirmation message.
User Login:

The user sends a username and password.
The server compares the password with the hashed password stored in the database.
If the login is successful, an access token (short-lived) and refresh token (long-lived) are issued.
JWT-Based Authentication:

The access token is used for authorization on protected routes.
The refresh token is used to obtain a new access token after it expires.

Authorization & Access Control
URL Management Endpoints
Create URL: /api/urls

Method: POST
Authorization: Required (access token)
Request Body:
json
Copy
Edit
{
  "originalUrl": "http://example.com"
}
View URLs: /api/urls

Method: GET
Authorization: Required (access token)
Response:
json
Copy
Edit
[
  {
    "id": 1,
    "originalUrl": "http://example.com",
    "shortenedUrl": "http://short.ly/abc123"
  },
  ...
]

Access Control:
Only authenticated users can access the URL management routes.
Each user can only view and manage their own URLs.
The system checks that the user ID from the JWT matches the owner of the URL being accessed.

I implemented Docker and Docker Composee for Backend

Docker file:
From openjdk:17
VOLUME /tmp
EXPOSE 3032
COPY target/spring-boot-security-jwt-0.0.1-SNAPSHOT.jar authentication.jar
ENTRYPOINT ["java", "-jar", "/authentication.jar"]

Docker Compose:
version: "3.8"

services:
  auth-db:
    image: postgres
    container_name: new-users-db
    restart: always
    environment:
      - POSTGRES_USER=admin
      - POSTGRES_PASSWORD=jado
      - POSTGRES_DB=qtdb
    ports:
      - '5435:5432'

  employee:
    container_name: springbootspringsecurityjwtauthentication
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - '3033:3032'
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://auth-db:5432/qtdb
      - SPRING_DATASOURCE_USERNAME=admin
      - SPRING_DATASOURCE_PASSWORD=jado
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
    depends_on:
      - auth-db
      
    restart: always
