# Moneta Media Application

## Overview

Moneta Media is a Spring Boot application designed to interface with a PostgreSQL database hosted on Render and Docker. This application demonstrates the use of Spring Boot and raw SQL queries to interact with the PostgreSQL database.

## Architecture Choices

The application is built using the following components:
- **Spring Boot**: Provides the foundation for the application, including dependency management, auto-configuration, and embedded web server.
- **Spring JDBC**: Used for executing raw SQL queries directly against the PostgreSQL database.
- **PostgreSQL**: A powerful, open-source object-relational database system used as the data store.

### Challenges

During development, one of the main challenges was configuring the application to connect to the PostgreSQL database hosted on Render and likewise Docker. The JDBC URL format required specific attention to ensure compatibility with the PostgreSQL driver.
Also, i had major issue with my spring security as it did not work as i expected and also that i initially did not authenticate all my endpoints which when i began to authenticate gave me lots of worries.
## Prerequisites

Before you begin, ensure you have met the following requirements:
- Java 11 or higher
- Maven 3.6.3 or higher
- PostgreSQL database credentials

## Setting Up the Application

1. **Clone the Repository**

   ```sh
   git clone https://github.com/your-repo/moneta-media.git
   cd moneta-media
Configure the Database

Edit the src/main/resources/application.properties file with your database details.

properties
Copy code
# Internal Database Configuration for Render
spring.datasource.url=jdbc:postgresql://dpg-cped1rf109ks73etuu6g-a/moneta_media_58ez
spring.datasource.username=moneta_media_58ez_user
spring.datasource.password=SGjIP4vv3Flg2RrA6inyZKZyRDVDQwbJ

# External Database Configuration for Render
# Uncomment and configure if needed
# external.spring.datasource.url=jdbc:postgresql://dpg-cped1rf109ks73etuu6g-a.oregon-postgres.render.com/moneta_media_58ez
# external.spring.datasource.username=moneta_media_58ez_user
# external.spring.datasource.password=SGjIP4vv3Flg2RrA6inyZKZyRDVDQwbJ

# Database Configuration for Docker
create your docker file
create your docker compose file also
adjust your application.properties to the environment you want to build on docker
Build the Application using docker-compose build

Once the application is running, you can access it at http://localhost:5050 and together with your various endpoints.

Troubleshooting
Database Connection Issues: Ensure that the database URL, username, and password are correctly configured in the application.properties file.
Port Conflicts: Make sure the default port 8080 is not occupied by another application. You can change the port by adding server.port=9090 (or any other port) to application.properties.
