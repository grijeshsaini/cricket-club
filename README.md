# Cricket Club Application
[![Java CI with Gradle](https://github.com/grijeshsaini/cricket-club/actions/workflows/gradle.yml/badge.svg)](https://github.com/grijeshsaini/cricket-club/actions/workflows/gradle.yml)

## Introduction
 This application is for cricket club group members to collect and pay fines and nominate members for awards.
 
## Local Development
To start the applications locally, please follow the below steps

- First, we need to build backend code using the command `./gradlew clean build`, this will run all the unit tests, integration tests and will create a Jar file inside the `build` directory.
- Then run `docker-compose up --build`, this command will create the image using the above Jar file and `Dockerfile` and then it will start the containers.
- Then access the api's on swagger `http://localhost:8080/swagger-ui/`.

## Technologies Used

- Java-16 (Will upgrade to Java 17 LTS once gradle start supporting it).
- Spring Boot
- MySql
- Docker
- Swagger
- Liquibase