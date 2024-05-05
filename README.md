# Java Practical Test Assignment - User Management API

This project is a RESTful API developed with Spring Boot for managing user entities. It allows operations such as creating, updating, and deleting users, as well as fetching users by various criteria.

## Features

The API provides the following features:

- **Create a New User**: Add a new user to the system.
- **Update an Existing User**: Modify details of an existing user.
- **Partially Update a User**: Update specific fields of a user.
- **Delete a User**: Remove a user from the system.
- **List Users by Birth Date Range**: Retrieve users whose birth dates fall within a specified range.
- **Find User by Email**: Get details of a user based on their email address.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them:

- JDK 11 or newer
- Maven 3.6 or newer

### Installing

1. Clone the repository:
```git@github.com:Mykola-Osolinskyi/Clear-Solutions-test-task.git```
2. Open a terminal in the project directory.
3. Run ```mvn clean install```.
4. Start the application using ```mvn spring-boot:run```.
5. Visit the Swagger UI to interact with the API:
```http://localhost:8080/swagger-ui/index.html```.

## Using the API

You can interact with the API using the Swagger UI, which is accessible via:
- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

This provides a friendly web interface to test all the API endpoints without the need for additional tools.

## Built With

- [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used
- [Maven](https://maven.apache.org/) - Dependency Management

## Authors

- [Mykola Osolinskyi](https://github.com/Mykola-Osolinskyi)





