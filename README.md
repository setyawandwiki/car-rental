# Car Rental Backend

A RESTful API backend for a car rental system, built with Java and Spring Boot. This project supports user authentication, car management, rental bookings, and payment integration using Xendit webhooks. It is designed to be scalable, secure, and easy to maintain.

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Testing](#testing)
- [Folder Structure](#folder-structure)
- [Contributing](#contributing)
- [License](#license)

## Features
- **User Authentication**: Register, login, and logout with JWT-based authentication.
- **Car Management**: CRUD operations for managing car details (e.g., model, availability, price).
- **Rental Booking**: Create, view, and manage car rental bookings.
- **Payment Integration**: Supports payments and disbursements via Xendit webhooks.
- **Role-Based Access**: Admin and user roles for restricted access.
- **Error Handling**: Comprehensive error handling and input validation.
- **API Documentation**: Interactive API documentation with Swagger UI.

## Tech Stack
- **Language**: Java (JDK 24)
- **Framework**: Spring Boot
- **Database**: PostgreSQL (production), Testcontainers (testing)
- **Authentication**: JSON Web Tokens (JWT)
- **API Documentation**: Swagger UI
- **Payment Gateway**: Xendit
- **Testing**: JUnit, Mockito, Testcontainers
- **Migration**: Flyway
- **Build Tool**: Maven
- **Containerization**: Docker (optional)

## Prerequisites
Ensure you have the following installed:
- [Java](https://www.oracle.com/java/) (JDK 17 or higher)
- [Maven](https://maven.apache.org/)
- [PostgreSQL](https://www.postgresql.org/) (local or cloud instance)
- [Docker](https://www.docker.com/) (optional, for containerized database)
- [Git](https://git-scm.com/)

## Installation
1. Clone the repository:
   ```bash
   https://github.com/setyawandwiki/car-rental.git

2. Install dependencies:
   ```bash
   mvn clean install

<h1>Configuration</h1>

1. Rename or update src/main/resources/application-example.properties into src/main/resources/application.properties 
   with the following:
    ```
   spring.application.name=car-rental

    server.servlet.context-path=/api/v1
    spring.profiles.active=development
    
    spring.datasource.url=jdbc:postgresql://localhost:5432/car_rental
    spring.datasource.username=[YOUR DB USERNAME]
    spring.datasource.password=[YOUR DB PASSWORD]
    spring.datasource.driver.class-name=org.postgresql.Driver
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    spring.jpa.show-sql=true
    spring.jpa.hibernate.ddl-auto=none
    spring.jpa.properties.hibernate.format_sql=true
    
    spring.flyway.enabled=false
    
    jwt.secret=[YOUR JWT SECRET]
    
    cloudinary.cloud_name=[YOUR CLOUD NAME ON CLOUDINARY]
    cloudinary.api_key=[YOUR CLOUDINARY API KEY]
    cloudinary.api_secret=[YOUR CLOUDINARY API SECRET]
    
    xendit.api-key=[YOUR XENDIT API KEY]
    xendit.public-key=[YOUR XENDIT PUBLIC KEY]
    
    springdoc.swagger-ui.enabled=true
    springdoc.swagger-ui.path=/swagger-ui
    springdoc.api-docs.path=/v3/api-docs
2. Rename or update src/main/resources/application-test-example.properties into 
   src/main/resources/application-test.properties
   with the following:
   ```
    spring.datasource.url=jdbc:tc:postgresql:15:///car_rental
    spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver
    spring.datasource.username=[username]
    spring.datasource.password=[password]
    
    spring.jpa.hibernate.ddl-auto=none
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    
    spring.flyway.enabled=true
    spring.flyway.clean-disabled=true
    spring.flyway.locations=classpath:db/migrations
    spring.flyway.baseline-on-migrate=true
   
3. Ensure .gitignore includes application.properties to avoid exposing sensitive data. 
   Use application-example.properties as a template.




