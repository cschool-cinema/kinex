ALTER TABLE movie
    ALTER COLUMN id SET DEFAULT nextval('movie_id_seq'::regclass);
ALTER TABLE auditorium
    ALTER COLUMN id SET DEFAULT nextval('auditorium_id_seq'::regclass);
ALTER TABLE seat
    ALTER COLUMN id SET DEFAULT nextval('seat_id_seq'::regclass);
ALTER TABLE screening
    ALTER COLUMN id SET DEFAULT nextval('screening_id_seq'::regclass);
ALTER TABLE ticket
    ALTER COLUMN id SET DEFAULT nextval('ticket_id_seq'::regclass);

INSERT INTO movie (category, duration_min, release_year, title)
VALUES ('Sci-Fi', 180, 1970, 'Star Wars IV');
INSERT INTO movie (category, description, duration_min, release_year, title)
VALUES ('Fantasy', 'whatever', 120, 2003, 'Wiedźmin');

INSERT INTO auditorium (name)
VALUES ('Jupyter');
INSERT INTO auditorium (name)
VALUES ('Uranus');

INSERT INTO seat (auditorium_id, seat_number, seat_row)
VALUES (1, 1, 1);
INSERT INTO seat (auditorium_id, seat_number, seat_row)
VALUES (1, 1, 2);
INSERT INTO seat (auditorium_id, seat_number, seat_row)
VALUES (1, 2, 1);
INSERT INTO seat (auditorium_id, seat_number, seat_row)
VALUES (1, 2, 2);

INSERT INTO seat (auditorium_id, seat_number, seat_row)
VALUES (2, 1, 1);
INSERT INTO seat (auditorium_id, seat_number, seat_row)
VALUES (2, 1, 2);
INSERT INTO seat (auditorium_id, seat_number, seat_row)
VALUES (2, 2, 1);
INSERT INTO seat (auditorium_id, seat_number, seat_row)
VALUES (2, 2, 2);

INSERT INTO screening (auditorium_id, movie_id, screening_start)
VALUES (1, 1, now() + '2 days'::interval);
INSERT INTO screening (auditorium_id, movie_id, screening_start)
VALUES (2, 2, now() + '1 seconds'::interval);
INSERT INTO screening (auditorium_id, movie_id, screening_start)
VALUES (2, 2, now() + '1 day'::interval);

INSERT INTO ticket(user_account_id, screening_id, seat_id)
VALUES (1, 1, 1);
INSERT INTO ticket(user_account_id, screening_id, seat_id)
VALUES (1, 1, 2);
INSERT INTO ticket(user_account_id, screening_id, seat_id)
VALUES (1, 1, 3);
INSERT INTO ticket(user_account_id, screening_id, seat_id)
VALUES (1, 1, 4);
INSERT INTO ticket(user_account_id, screening_id, seat_id)
VALUES (2, 2, 5);