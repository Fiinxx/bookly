-- default admin user pw: admin
insert into users (id, password, role, username)
VALUES (1, '$2y$10$jUHAszra9BeurVZ4P9GN0O3nYME.AkM3DcZdLDtEuFf4BD.SufdHu', 'ADMIN', 'admin');
--ALTER SEQUENCE users_id_seq RESTART WITH 2;