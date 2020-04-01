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
The result of the following rest api is table with provided secret and decrypted and encrypted text: 

| Secret password | @x8HcZsUlfdE  |         
| -------------   |:-------------:|
| Decrypted text  | @Java2019     |
| Encrypted text  | O8PUiDHzgAKmzQpKap+zxqnltgfMhxpI373/nnhghxjLuMJHyOuV8ya9tG9QM0TX |
 

The securing of the data can be done by putting encrypted sensitive data in the ```application.properties``` 
file inside tag ```ENC(``` ```yours encrypted data``` ```)```.

Data for the working sample program setup:
- secret password for application is ```@x8HcZsUlfdE``` 
- database user is ```kinex_user```
- used password is ```termos2137```

    As an email service provider <a href="">mailtrap.io</a> is used.
    Sample credentials for test account is ass follows:
    (this data provided only in testing purposes, number of emails limited to 50)
    ```
        Host:      smtp.mailtrap.io
        Port:      2525
        Username:  80087d6e5a6c67
        Password:  66c9872dbe4957   
    ```

 - JWT secret password is ```j2uMNsCBQPvA8rQX```



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

### Database setup, SQL queries and functions

- Run ```create-user-and-db.sql``` and next ```functions.sql``` or copy and run in PSQL terminal.
    - Use DROP option if you want clean database creation.
    
- SQL files provided in the folder ```sql```.
    - Create database and user as follows, please choose your own credentials:
    
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
    
    - SQL functions
        ```
            CREATE OR REPLACE FUNCTION
                is_screening_conflict(pscreening_start timestamp WITH TIME ZONE,
                                      pscreening_end timestamp WITH TIME ZONE,
                                      pauditorium_id int)
                RETURNS boolean AS
            $$
            BEGIN
                RETURN (
                    CASE
                        WHEN (SELECT COUNT(s.id)
                              FROM screening s
                                       JOIN movie mv ON s.movie_id = mv.id
                              WHERE s.auditorium_id = pauditorium_id
                                AND s.screening_start < pscreening_end
                                AND s.screening_start + (INTERVAL '1 minute' * mv.duration_min) + INTERVAL '15 minutes' >
                                    pscreening_start) > 0
                            THEN TRUE
                        ELSE FALSE
                        END);
            END;
            $$
                LANGUAGE PLPGSQL;
            
            ---------------------------------------------------
            
            CREATE OR REPLACE FUNCTION
                get_screening_end(pscreening_id int) RETURNS timestamp WITH TIME ZONE AS
            $$
            BEGIN
                RETURN (
                    SELECT (s.screening_start + (INTERVAL '1 minute' * mv.duration_min) + INTERVAL '15 minutes') screening_end
                    FROM screening s
                             JOIN movie mv ON s.movie_id = mv.id
                    WHERE s.id = pscreening_id);
            END;
            $$
                LANGUAGE PLPGSQL;
        ```
- ```data.sql``` for application proper run is in the resource folder, sample inserts for test are commented, please uncomment to use it. 
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