INSERT INTO user_service.user (id, email, first_name, last_name, password, roles)
VALUES (1, 'rafael@gmail.com', 'Rafael3', 'Roani Gonçalves2', '', 'USER');
INSERT INTO user_service.user (id, email, first_name, last_name, password, roles)
VALUES (4, 'rafael@gmail.co', 'Rafael', 'Roani Gonçalves', '', 'USER');
INSERT INTO user_service.user (id, email, first_name, last_name, password, roles)
VALUES (5, 'rafaelteste@gmail.com', 'Rafael', 'Roani Gonçalves', '{bcrypt}$2a$10$dYkEywGPoamKf0m0KI0NCulvjoszPL/gm/ogSoxL/AdgHAmAxymeu',
        'USER,ADMIN');

INSERT INTO user_service.profile (id, description, name)
VALUES (1, 'Regular user with regular permissions', 'Regular User');

INSERT INTO user_service.user_profile (id, profile_id, user_id)
VALUES (1, 1, 1);

