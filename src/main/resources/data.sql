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
ALTER TABLE user_account
    ALTER COLUMN id SET DEFAULT nextval('user_account_id_seq'::regclass);
ALTER TABLE activity_log
    ALTER COLUMN id SET DEFAULT nextval('activity_log_id_seq'::regclass);

INSERT INTO user_account(activated_at, activation_uuid, created_at, email, first_name, is_authenticated, is_enabled,
                         last_name, password, role, salt, username, valid_account_till, valid_password_till)
VALUES ('2020-01-26 13:52:10.666', 'de980b3a-1f71-41fd-b0dc-2d2abc2ae8c6', '2020-01-26 13:50:58.088',
        'owner@kinex.com', 'Owner', FALSE, TRUE, 'Owner', 'de67v5bC9a4sk', 'ROLE_OWNER',
        'de980b3a1f7141fdb0dc2d2abc2ae8c6', 'ps', '2021-01-26 13:50:56.09', '2021-01-26 13:50:56.091');
INSERT INTO user_account(activated_at, activation_uuid, created_at, email, first_name, is_authenticated, is_enabled,
                         last_name, password, role, salt, username, valid_account_till, valid_password_till)
VALUES ('2020-01-29 17:59:15.442000', '6e618ff3-fb1e-4377-9526-73fdbda848c1', '2020-01-29 17:58:33.875000',
        'cipemoy946@repshop.net', 'heavy', FALSE, TRUE, 'user', '6epWL2w74Y3Ow', 'ROLE_USER',
        '6e618ff3fb1e4377952673fdbda848c1', 'hu', '2021-01-29 17:58:30.438000', '2021-01-29 17:58:30.438000');

INSERT INTO movie (category, duration_min, release_year, title)
VALUES ('Sci-Fi', 180, 1970, 'Star Wars IV');
INSERT INTO movie (category, description, duration_min, release_year, title)
VALUES ('Fantasy', 'whatever', 120, 2003, 'Wiedźmin');

INSERT INTO auditorium (name)
VALUES ('Jupyter');
INSERT INTO auditorium (name)
VALUES ('Uranus');
INSERT INTO auditorium (name, active)
VALUES ('Mars', FALSE);

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

INSERT INTO seat (auditorium_id, seat_number, seat_row)
VALUES (3, 1, 1);
INSERT INTO seat (auditorium_id, seat_number, seat_row)
VALUES (3, 1, 2);
INSERT INTO seat (auditorium_id, seat_number, seat_row)
VALUES (3, 2, 1);
INSERT INTO seat (auditorium_id, seat_number, seat_row)
VALUES (3, 2, 2);

INSERT INTO screening (auditorium_id, movie_id, screening_start)
VALUES (1, 1, now() + '2 days'::interval);
INSERT INTO screening (auditorium_id, movie_id, screening_start)
VALUES (3, 2, now() + '1 seconds'::interval);
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
VALUES (2, 2, 6);