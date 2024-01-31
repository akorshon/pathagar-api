# pathagar-api  

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=akorshon_pathagar-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=akorshon_pathagar-api)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=akorshon_pathagar-api&metric=bugs)](https://sonarcloud.io/summary/new_code?id=akorshon_pathagar-api)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=akorshon_pathagar-api&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=akorshon_pathagar-api)

"Pathagar" (পাঠাগার) is a Bengali word meaning "library". The aim of this project is to maintain a library (mostly pdf books).

## Technology Stack
* Kotlin
* Gradle
* Jvm 17
* Spring Boot
  * Spring Data JPA
  * Spring Security
  * Spring MVC
  * Spring REST
  * Spring Test
* MySQL
* Docker

## How to run
* Clone the project
* Run `docker-compose up -d` to start the database
* Run `./gradlew bootRun` to start the application

## How to test

## How to deploy
`gradle clean build`

