DROP USER IF EXISTS kinex_user;
CREATE USER kinex_user WITH PASSWORD 'termos2137';


DROP DATABASE IF EXISTS kinex;
CREATE DATABASE kinex
    WITH
    OWNER = kinex_user
    ENCODING = 'UTF8'
    LC_COLLATE = 'pl_PL.utf8' --alternatively 'pl_PL' on Windows
    LC_CTYPE = 'pl_PL.utf8' --alternatively 'pl_PL' on Windows
    TEMPLATE = template0
    CONNECTION LIMIT = 5000;