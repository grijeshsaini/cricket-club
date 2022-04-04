# Cricket Club Application
[![Java CI with Gradle](https://github.com/grijeshsaini/cricket-club/actions/workflows/gradle.yml/badge.svg)](https://github.com/grijeshsaini/cricket-club/actions/workflows/gradle.yml)

## Introduction
 This application is for cricket club group members.
 
## Local Development
To start the applications locally, please follow the below steps

- First, we need to build backend code using the command `./gradlew clean build`, this will run all the unit tests, integration tests.
- Then run `docker-compose up --build`, this command will create the image using the above Jar file and `Dockerfile` and then it will start the containers.
- Then access the apis on swagger `http://localhost:8080/swagger-ui/`.

## Technologies Used

- Java-17.
- Spring Boot
- MySql
- Docker
- Swagger
- Liquibase
- TestContainers (To Write integration test.)
