toolrental is an application for generating a rental agreement for renting a tool, built with Spring Boot.

## Technologies Used
- Java 19
- Spring Boot 3.2.3
- Maven

## Component diagram

![component diagram](./images/component-diagram.png?raw=true)

## Building the application

To build the application, you can use the following command:
```
mvn clean package
```
This will generate a jar file called ```toolrental.jar``` in the target directory.

## Getting Started
To run the application locally, you can use the following command:
### command line mode
```
java -jar toolrental.jar

OR

java -jar toolrental.jar --mode cli
```
### web service mode
```
java -jar toolrental.jar --mode web
```

NOTE: The web service can be reached at [http://localhost:8080/checkout.html](http://localhost:8080/checkout.html).

## Features
- User can create a rental agreement via a command line or a web interface
- Validation of user input based on requirements

## Future Improvements
- Implement user authentication
- Move data to a database and use Spring JPA
- Add payment processing functionality
- Enhance the user interface

## Screenshots

### Command Line Interface

![CLI](./images/cli.png?raw=true)

### Web Interface

![Web](./images/web.png?raw=true)

