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

# REQUESTS

# Application sequence actions
During first run ```USER``` needs to register application ```OWNER``` account. The sequence is as follows:

1. Register using user owner registration request  
2. After success user will receive in response the activation token on the provided email
    - token is valid ```25 min``` in default 
3. Activate the account using provided activation token
    - if activation is performed after ```25 minutes``` of token receive, ```new token will be sent on provided email```
    - go to step 3 again with a new token
4. User can authenticate with provided credentials 
5. If authentication is succeed, user receive jwt token for using api. Now user can perform all possible actions described in postman requests provided in folder ```POSTMAN``` 

After OWNER is registered REST API allows registering and manage all other possible roles:
```
    1. USER
    2. MANAGER
    3. ADMINISTRATOR
    4. OWNER
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

### SECURITY
In the program <a href="http://www.jasypt.org/cli.html">Jasypt</a> encryption have been used to secure the direct input of the 
sensitive data (credentials etc.). Idea was to encrypt this data using <a href="http://www.jasypt.org/cli.html">Jasypt</a> library.
The encryption-decryption could be done using directly <a href="http://www.jasypt.org/cli.html">Jasypt</a> cli  or
by using provided REST application 
<a href="http://www.jasypt.org/cli.html">```https://github.com/DimaLumelskyj/password.generator.jasypt.git```</a>.
The result of the following rest api is the table with provided secret and decrypted and encrypted text: 

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
    Sample credentials for test account is ass follows
    ```(this data provided only in testing purposes, number of emails limited to 50, please make own account if currrent will not work)```:
    
    ```
        Host:      smtp.mailtrap.io
        Port:      2525
        Username:  80087d6e5a6c67
        Password:  66c9872dbe4957   
    ```

 - JWT secret password is ```j2uMNsCBQPvA8rQX```
 - Secret password can be passed in two ways:
    - by argument `-Djasypt.encryptor.password=@x8HcZsUlfdE` 
    - as option in maven `pom.xml`
    ```
        <configuration>
            <jvmArguments>
                -Djasypt.encryptor.password=@x8HcZsUlfdE
            </jvmArguments>
        </configuration>
    ```
### Database setup, SQL queries and functions

- Run from the <a href="https://github.com/cschool-cinema/kinex/tree/master/sql">folder</a> ```create-user-and-db.sql``` and next ```functions.sql``` or copy and run in PSQL terminal.
    - Use DROP option if you want clean database creation.
    
- SQL files provided in the folder ```sql```.
    - Create the database and user as follows, please choose your own credentials:
    
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

In all cases please add VM option with the master decryption password or add in pom.xml during development and testing. 
 
Using IDEA

The text `@x8HcZsUlfdE` is yours sample master password.

```
Set the VM options: -Djasypt.encryptor.password=@x8HcZsUlfdE
Run the main method from Kinex application (KinexApplication.main())
```

Packaging the application as a JAR and run it
```
mvn clean package && java -jar target/kinex-0.0.1-SNAPSHOT.jar -Djasypt.encryptor.password=@x8HcZsUlfdE
```

Using Maven Spring Boot plugin
```
mvn spring-boot:run
```
##TEST DATA
User test data in `data.sql`: 

```
all users have the same password: @sdRda27dDF
```
```
+--------+------------------+
|username|role              |
+--------+------------------+
|guest   |ROLE_GUEST        | 
|Vobek   |ROLE_OWNER        |
|Dobek   |ROLE_OWNER        |
|Gobek   |ROLE_ADMINISTRATOR|
|Nobek   |ROLE_MANAGER      |
|Sobek   |ROLE_MANAGER      |
|Pobek   |ROLE_ADMINISTRATOR|
|Kobek   |ROLE_USER         |
|Hobek   |ROLE_USER         |
+--------+------------------+
```
## Authors
* [Paweł Szopinski]
* [Dmytro Lumelskyj] 
* [Jacek Gas]