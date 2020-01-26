-- DROP USER kinex_user;
-- CREATE USER kinex_user WITH PASSWORD 'termos2137';


-- DROP DATABASE kinex;
--CREATE DATABASE kinex
-- 	WITH
-- 	OWNER = kinex_user
-- 	ENCODING = 'UTF8'
-- 	LC_COLLATE = 'pl_PL.utf8' --alternatively 'pl_PL' or 'pl_PL.utf8' if on Linux
-- 	LC_CTYPE = 'pl_PL.utf8' --alternatively 'pl_PL' or 'pl_PL.utf8' if on Linux
-- 	TEMPLATE=template0
-- 	CONNECTION LIMIT = 5000;


-- I did not include columns like cast, director, etc., since it's not the point of this exercise.
-- for the same reason category is a regular text column, since we'd have to add many to many relationship (let me know if you think it'd be a good idea to include it anyway)
DROP TABLE IF EXISTS movie CASCADE;
CREATE TABLE movie
(
    id           serial       NOT NULL PRIMARY KEY,
    title        varchar(256) NOT NULL,
    release_year smallint CHECK (release_year BETWEEN 1800 AND date_part('year', CURRENT_DATE) + 5),
    category     varchar(256),
    description  text,
    duration_min smallint     NOT NULL CHECK (duration_min BETWEEN 1 AND 1000)
);


-- one of the least complex tables, defines auditorium (room)
DROP TABLE IF EXISTS auditorium CASCADE;
CREATE TABLE auditorium
(
    id   smallserial NOT NULL PRIMARY KEY,
    name varchar(32) NOT NULL
);


-- defines single movie screening
-- pay attention to unique constraint - an employee won't be able to create new screening entry if it starts at the same time in the same auditorium
---- what is more, in Java code we'll have to be even more strict in defining that business rule (in database it's not that easy and we'd probably have to set up some kind of a trigger)
---- the business rule would be to allow a new screening entry only if this condition is met (previous/most recent screening_start in the same auditorium + movie.duration_min + e.g. 15 min for cleaning < new screening_start)
---- TODO: it might be possible to build custom function in db to also check all of the above
DROP TABLE IF EXISTS screening CASCADE;
CREATE TABLE screening
(
    id              serial    NOT NULL PRIMARY KEY,
    movie_id        int       NOT NULL REFERENCES movie (id),
    auditorium_id   smallint  NOT NULL REFERENCES auditorium (id),
    screening_start timestamp NOT NULL CHECK (screening_start > now()),
    CONSTRAINT screening_auditorium_id_screening_start_key UNIQUE (auditorium_id, screening_start)
);


-- defines seats available in auditoriums
-- let's say that our biggest auditorium can be of size 100x100 (let's agree on rectangular shape auditoriums only)
-- unique constraint makes sure that there are no multiple records with the same number/row in one auditorium
-- TODO: think about how employee could remove/deactivate seats in auditorium (can they do single seats or whole rows only? I'd vote for rows only,
---- since our auditorium structure only allows rectangular shapes)
DROP TABLE IF EXISTS seat CASCADE;
CREATE TABLE seat
(
    id            serial   NOT NULL PRIMARY KEY,
    auditorium_id smallint NOT NULL REFERENCES auditorium (id),
    seat_row      smallint NOT NULL CHECK (seat_row BETWEEN 1 AND 100),
    seat_number   smallint NOT NULL CHECK (seat_number BETWEEN 1 AND 100),
    CONSTRAINT seat_auditorium_id_seat_row_seat_number_key UNIQUE (auditorium_id, seat_row, seat_number)
);


-- case insensitive extension
-- unfortunately PostgreSQL does not allow creating check constraint like lower(email)
---- we will also use ToLower function in Java before inserting record
-- in order to achieve similar results a case insensitive type column has to be used
CREATE EXTENSION IF NOT EXISTS citext;

-- this table will hold both users of any type (employees and customers)
-- user_type column will define if a person is employee (2) or client (1)
---- if you think that it'd be better to have two separate tables - let me know
-- password is varchar, but will store hashes, not plain text, if needed we can later add salt column - this is for our security guy - Dima - to confirm
-- TODO: decide if password hashing will be done by db or in Java code (Java is more secure, since we're not passing plain pass text to db)
DROP TABLE IF EXISTS user_account CASCADE;
CREATE TABLE user_account
(
    id         serial       NOT NULL PRIMARY KEY,
    email      citext       NOT NULL UNIQUE CHECK (email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$' AND
                                                   LENGTH(email) < 128),
    password   varchar(128) NOT NULL,
    first_name varchar(128) NOT NULL,
    last_name  varchar(128) NOT NULL,
    user_type  smallint     NOT NULL CHECK (user_type BETWEEN 1 AND 2)
);


-- this table defines reservations/tickets
-- added column active (if we wanted to add ability to cancel reservations later)
-- user_id can be either client or employee (employees can also purchase tickets for themselves) - this is another argument for one user table
DROP TABLE IF EXISTS reservation CASCADE;
CREATE TABLE reservation
(
    id              serial NOT NULL PRIMARY KEY,
    screening_id    int    NOT NULL REFERENCES screening (id),
    user_account_id int    NOT NULL REFERENCES user_account (id),
    active          bool   NOT NULL DEFAULT TRUE
);

-- second most important table; holds reserved seat details for a given reservation
-- TODO: is it feasible to remove screening_id column from here (we have it in reservation, which is "parent" table for seat_reserved)
----- if so, how to rebuild unique constraint that has to be in place for seat/screening?
DROP TABLE IF EXISTS seat_reserved CASCADE;
CREATE TABLE seat_reserved
(
    id             serial NOT NULL PRIMARY KEY,
    seat_id        int    NOT NULL REFERENCES seat (id),
    reservation_id int    NOT NULL REFERENCES reservation (id),
    screening_id   int    NOT NULL REFERENCES screening (id),
    CONSTRAINT seat_reserved_seat_id_screening_id_key UNIQUE (seat_id, screening_id)
);


-- GENERAL ISSUES
-- 1 think about how to go about an employee removing data, e.g. seats from auditorium where there are already reservations booked on these seats
---- or removing a movie where there are screenings already created with these movies
---- or removing auditoriums where there are past/future screenings
---- one way to go about it is to add "active" column to most of these tables, but that would add a lot of overhead
---- another option is to simply disallow removing records that have other related records
-- 2 I am not sure what they meant here "(Opcjonalnie z gwiazdka) możliwość kupowania biletów wyłącznie sąsiadujących", so we'll need to discuss
---- if there are any modifications required in db to accommodate it
-- 3 how to enforce at least e.g. one record in seat_reserved table for a reservation
---- or at least one row ow seats in auditorium
---- this might be a hint: https://stackoverflow.com/questions/20573407/one-mandatory-to-many-mandatory-relationship-in-sql
-- 4 UUIDs instead of ints at least in user and reservation tables
-- 5 check constraint on Ticket table (whether seat_id is in fact located in a given screening auditorium)
