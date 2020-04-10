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

-- INSERT INTO user_account(activated_at, activation_uuid, created_at, email, first_name, is_authenticated, is_enabled,
--                          last_name, password, role, salt, username, valid_account_till, valid_password_till)
-- VALUES ('2020-01-26 13:52:10.666', 'de980b3a-1f71-41fd-b0dc-2d2abc2ae8c6', '2020-01-26 13:50:58.088',
--         'owner@kinex.com', 'Owner', FALSE, TRUE, 'Owner', 'de67v5bC9a4sk', 'ROLE_OWNER',
--         'de980b3a1f7141fdb0dc2d2abc2ae8c6', 'ps', '2021-01-26 13:50:56.09', '2021-01-26 13:50:56.091'),
--        ('2020-01-29 17:59:15.442000', '6e618ff3-fb1e-4377-9526-73fdbda848c1', '2020-01-29 17:58:33.875000',
--         'cipemoy946@repshop.net', 'heavy', FALSE, TRUE, 'user', '6epWL2w74Y3Ow', 'ROLE_USER',
--         '6e618ff3fb1e4377952673fdbda848c1', 'hu', '2021-01-29 17:58:30.438000', '2021-01-29 17:58:30.438000');

INSERT INTO public.user_account (id, activated_at, activation_uuid, created_at, email, first_name, is_authenticated,
                                 is_deleted, is_enabled, last_name, password, role, salt, username, valid_account_till,
                                 valid_password_till)
VALUES (1, '2020-04-10 16:48:32.019243', 'd9a8b57c-f889-4c13-975d-c07d08540a66', '2020-04-10 16:48:32.014778',
        'guest@email.com', 'guest', false, false, true, 'guest', 'd9Rvfne/enK2s', 'ROLE_GUEST',
        'd9a8b57cf8894c13975dc07d08540a66', 'guest', '2021-04-10 16:48:31.877386', '2021-04-10 16:48:31.877882');
INSERT INTO public.user_account (id, activated_at, activation_uuid, created_at, email, first_name, is_authenticated,
                                 is_deleted, is_enabled, last_name, password, role, salt, username, valid_account_till,
                                 valid_password_till)
VALUES (2, '2020-04-10 14:50:15.432342', '5fa5bebd-dd19-45b0-99c1-1e6184d99466', '2020-04-10 14:49:08.317713',
        'wepim48326@itiomail.com', 'Bobek', false, false, true, 'Bobek', '5f4jvklMhd/Es', 'ROLE_OWNER',
        '5fa5bebddd1945b099c11e6184d99466', 'Vobek', '2021-04-10 14:49:08.314241', '2021-04-10 14:49:08.314241');
INSERT INTO public.user_account (id, activated_at, activation_uuid, created_at, email, first_name, is_authenticated,
                                 is_deleted, is_enabled, last_name, password, role, salt, username, valid_account_till,
                                 valid_password_till)
VALUES (3, '2020-04-10 14:52:38.250278', 'd4de85bf-bf1b-4181-b812-197a5b3f1763', '2020-04-10 14:51:56.305900',
        'wohepim48326@itiomail.com', 'Kobek', false, false, true, 'Aobek', 'd4OktPcKrWWpM', 'ROLE_OWNER',
        'd4de85bfbf1b4181b812197a5b3f1763', 'Dobek', '2021-04-10 14:51:56.304909', '2021-04-10 14:51:56.304909');
INSERT INTO public.user_account (id, activated_at, activation_uuid, created_at, email, first_name, is_authenticated,
                                 is_deleted, is_enabled, last_name, password, role, salt, username, valid_account_till,
                                 valid_password_till)
VALUES (4, '2020-04-10 14:54:40.210508', '40a84e2d-1c82-4c75-876a-0e171d033dc6', '2020-04-10 14:53:25.833744',
        'gjgjwepim48326@itiomail.com', 'Bobek', false, false, true, 'Bobek', '40f3ItWgLCXFg', 'ROLE_ADMINISTRATOR',
        '40a84e2d1c824c75876a0e171d033dc6', 'Gobek', '2021-04-10 14:53:25.832752', '2021-04-10 14:53:25.832752');
INSERT INTO public.user_account (id, activated_at, activation_uuid, created_at, email, first_name, is_authenticated,
                                 is_deleted, is_enabled, last_name, password, role, salt, username, valid_account_till,
                                 valid_password_till)
VALUES (6, '2020-04-10 14:57:58.034122', 'be24f133-b532-4ace-96d8-228b16455a96', '2020-04-10 14:57:24.633045',
        'gjg326@itiomail.com', 'Bobek', false, false, true, 'Bobek', 'beCa7Z/EDJqmY', 'ROLE_MANAGER',
        'be24f133b5324ace96d8228b16455a96', 'Nobek', '2021-04-10 14:57:24.632053', '2021-04-10 14:57:24.632053');
INSERT INTO public.user_account (id, activated_at, activation_uuid, created_at, email, first_name, is_authenticated,
                                 is_deleted, is_enabled, last_name, password, role, salt, username, valid_account_till,
                                 valid_password_till)
VALUES (7, '2020-04-10 14:59:15.341733', '86af2608-79d3-46ec-a65d-afd5cb20c7c0', '2020-04-10 14:58:10.681278',
        'gjg3d26@itiomail.com', 'Bobek', false, false, true, 'Bobek', '86btAObWS0YF.', 'ROLE_MANAGER',
        '86af260879d346eca65dafd5cb20c7c0', 'Sobek', '2021-04-10 14:58:10.680287', '2021-04-10 14:58:10.680287');
INSERT INTO public.user_account (id, activated_at, activation_uuid, created_at, email, first_name, is_authenticated,
                                 is_deleted, is_enabled, last_name, password, role, salt, username, valid_account_till,
                                 valid_password_till)
VALUES (9, '2020-04-10 15:03:15.777252', 'd6858b96-78d9-49eb-80fa-8fc9b429776e', '2020-04-10 15:02:51.737325',
        'gjtyepfsf48326@itiomail.com', 'Bobek', false, false, true, 'Bobek', 'd6tcFHQLNB9hs', 'ROLE_ADMINISTRATOR',
        'd6858b9678d949eb80fa8fc9b429776e', 'Pobek', '2021-04-10 15:02:51.735837', '2021-04-10 15:02:51.735837');
INSERT INTO public.user_account (id, activated_at, activation_uuid, created_at, email, first_name, is_authenticated,
                                 is_deleted, is_enabled, last_name, password, role, salt, username, valid_account_till,
                                 valid_password_till)
VALUES (8, '2020-04-10 15:02:26.172569', 'a296d347-5dbe-4b1e-8b53-cf53b4c9eaed', '2020-04-10 15:01:37.619334',
        'gjtyepim48326@itiomail.com', 'Bobek', false, false, true, 'Bobek', 'a2EeLwjq4OweU', 'ROLE_USER',
        'a296d3475dbe4b1e8b53cf53b4c9eaed', 'Kobek', '2021-04-10 15:01:37.617847', '2021-04-10 15:01:37.618343');
INSERT INTO public.user_account (id, activated_at, activation_uuid, created_at, email, first_name, is_authenticated,
                                 is_deleted, is_enabled, last_name, password, role, salt, username, valid_account_till,
                                 valid_password_till)
VALUES (5, '2020-04-10 14:56:12.890106', '80ec4d94-4b33-46af-85ac-500ff9edacf6', '2020-04-10 14:55:35.644210',
        'gjgjwepim486@itiomail.com', 'Bobek', false, false, true, 'Bobek', '80apHTL3Irryo', 'ROLE_USER',
        '80ec4d944b3346af85ac500ff9edacf6', 'Hobek', '2021-04-10 14:55:35.642723', '2021-04-10 14:55:35.642723');

INSERT INTO movie (category, description, duration_min, release_year, title)
VALUES ('Sci-Fi', NULL, 180, 1970, 'Star Wars IV'),
       ('Fantasy', 'whatever', 120, 2003, 'Witcher'),
       ('Crime/Drama', NULL, 154, 1994, 'Pulp Fiction');

INSERT INTO auditorium (name, active)
VALUES ('Jupyter', TRUE),
       ('Uranus', TRUE),
       ('Mars', FALSE);

INSERT INTO seat (auditorium_id, seat_number, seat_row)
VALUES (1, 1, 1),
       (1, 2, 1),
       (1, 3, 1),
       (1, 4, 1),
       (1, 5, 1),
       (1, 6, 1),
       (1, 7, 1),
       (1, 8, 1),
       (1, 9, 1),
       (1, 10, 1),
       (1, 1, 2),
       (1, 2, 2),
       (1, 3, 2),
       (1, 4, 2),
       (1, 5, 2),
       (1, 6, 2),
       (1, 7, 2),
       (1, 8, 2),
       (1, 9, 2),
       (1, 10, 2),
       (2, 1, 1),
       (2, 2, 1),
       (2, 1, 2),
       (2, 2, 2),
       (3, 1, 1),
       (3, 2, 1),
       (3, 1, 2),
       (3, 2, 2);

INSERT INTO screening (auditorium_id, movie_id, screening_start)
VALUES (1, 1, now() + '2 days'::interval),
       (2, 2, now() + '1 seconds'::interval),
       (3, 3, now() + '1 day'::interval);

--INSERT INTO ticket(user_account_id, reserved_by_user_id, screening_id, seat_id, active, created_at)
--VALUES (1, 1, 1, 1, FALSE, '2020-01-29 17:58:33.875000'),
--       (1, 1, 1, 3, TRUE, '2020-01-29 17:58:33.875000'),
--       (1, 1, 1, 4, TRUE, '2020-01-29 17:58:33.875000'),
--       (2, 2, 2, 21, TRUE, '2020-01-29 17:58:33.875000'),
--       (1, 1, 2, 22, TRUE, '2020-01-29 17:58:33.875000');