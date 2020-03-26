<h1 align="center">
  <img src="kinex-logo.png" alt="Cinema" width="200">
  <br>
  KINEX Cinema Rest API
  <br>
</h1>

# Description

Cinema Rest API project for 
- Demonstrating the use of Hibernate and Spring Data JPA
- Applying Spring Security with JWT token

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

```
Java JDK (8+)
Maven
Git
PostgreSQL
Lombok plugin for IDEA
```

### Installing

Cloning project

```
git clone https://github.com/cschool-cinema/kinex.git
```

## Deployment

This Spring Boot application can be started in a few ways

Using IDEA

```
Run the main method from Kinex application (KinexApplication.main())
```

Packaging the application as a JAR and run it
```
mvn clean package && java -jar target/kinex-0.0.1-SNAPSHOT.jar
```

Using Maven Spring Boot plugin
```
mvn spring-boot:run
```

## Things

### One to One relationship
- A foreign key column is created in owner entity, in our example, Showtime owns ShowtimeSystem, so @JohnColumn will be used in Showtime entity.
- Foreign key column's name is the concatenation of the name of relationship in the owner side, _ , and then name of the primary key column(s) in the owned side.
- The owner is responsible for the association column(s) update, a side as not responsible for the relationship will use attribute mappedBy
- Besides that, we have other approaches for OneToOne at [here](https://howtodoinjava.com/hibernate/hibernate-one-to-one-mapping-using-annotations/)


## Authors
* [Paweł Szopinski]
* [Dmytro Lumelskyj] 
* [Jacek Gas]