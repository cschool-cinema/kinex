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

#REQUESTS

#SECURITY
In program <a href="http://www.jasypt.org/cli.html">Jasypt</a> encryption have been used to secure the direct input of the 
sensitive data (credentials etc.). Idea was to encrypt this data using <a href="http://www.jasypt.org/cli.html">Jasypt</a> library.
The encryption-decryption could be done using directly <a href="http://www.jasypt.org/cli.html">Jasypt</a> cli  or
by using provided REST application 
<a href="http://www.jasypt.org/cli.html">```https://github.com/DimaLumelskyj/password.generator.jasypt.git```</a>.
The securing of the data can be done by putting encrypted sensitive data in the ```application.properties``` file into
tag ```ENC(```<font color="red">```yours secured data```</font>```)```.

# SQL queries and backup data

Create database and user as follows, please setup password:
```
DROP USER IF EXISTS kinex_user;
CREATE USER kinex_user WITH PASSWORD 'termos2137';


DROP DATABASE IF EXISTS kinex;
CREATE DATABASE kinex
    WITH
    OWNER = kinex_user
    ENCODING = 'UTF8'
    LC_COLLATE = 'pl_PL.utf8' --alternatively 'pl_PL' or 'pl_PL.utf8' if on Linux
    LC_CTYPE = 'pl_PL.utf8' --alternatively 'pl_PL' or 'pl_PL.utf8' if on Linux
    TEMPLATE = template0
    CONNECTION LIMIT = 5000;
```

# Entity relationship diagram
The ER diagram that fits domain classes looks as follows:

<img src="ER-Diagram.svg" alt="ER-DIAGRAM" class="width: 25%;">

# Getting Started

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

### Database and user create

- Run Covid19DatabaseWithOwnerUserCreate.sql or copy and run in PSQL terminal.
    - Use DROP option if you want clean database creation.

```
DROP DATABASE IF EXISTS covid19;
DROP USER IF EXISTS covid19_user;
CREATE USER covid19_user WITH PASSWORD 'user2313';

CREATE DATABASE covid19
    WITH
    OWNER = covid19_user
    ENCODING = 'UTF8'
    LC_COLLATE = 'pl_PL.utf8' --alternatively 'pl_PL' or 'pl_PL.utf8' if on Linux
    LC_CTYPE = 'pl_PL.utf8' --alternatively 'pl_PL' or 'pl_PL.utf8' if on Linux
    TEMPLATE = template0
    CONNECTION LIMIT = 5000;
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


## Authors
* [Paweł Szopinski]
* [Dmytro Lumelskyj] 
* [Jacek Gas]