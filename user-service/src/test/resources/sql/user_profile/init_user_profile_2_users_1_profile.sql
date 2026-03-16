INSERT INTO `user` (id, email, first_name, last_name, roles, password)
VALUES (1, 'yusuke@yuyuhakusho.com', 'Yusuke', 'Urameshi', 'USER',
        '{bcrypt}$2a$10$HS5NeuWolqLsVme8iupNveWUYFd.5fUsHy3rOcEBWFALtmUm5bZr2');
INSERT INTO `user` (id, email, first_name, last_name, roles, password)
VALUES (2, 'hiei@yuyuhakusho.com', 'Hiei', 'Dragon', 'USER',
        '{bcrypt}$2a$10$HS5NeuWolqLsVme8iupNveWUYFd.5fUsHy3rOcEBWFALtmUm5bZr2');

INSERT INTO profile (id, description, name)
VALUES (1, 'Manages everything', 'Admin');

INSERT INTO user_profile (id, profile_id, user_id)
VALUES (1, 1, 1);
INSERT INTO user_profile (id, profile_id, user_id)
VALUES (2, 1, 2);