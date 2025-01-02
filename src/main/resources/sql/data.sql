-- DML

INSERT INTO manufacturer (name)
VALUES ('Samsung'),
       ('Dell'),
       ('HP'),
       ('Apple'),
       ('Lenovo'),
       ('Acer'),
       ('AMD');

INSERT INTO client (name, email, password, card)
VALUES ('Mercury', 'mercury@gmail.com', '$2a$12$pUy28qlPDaGVaV74OWDZ8ufFJvbfZrdR87fKroY1DRPhFrdOVHxcq', '5326-1111'),
       ('Venus', 'venus@gmail.com', '$2a$12$VnIPr7BnHhPqo5ZHuPxC5e.RnqcL1m03rB1WHtN67VB/CXd22B7Hi', '5326-2222'),
       ('Earth', 'earth@gmail.com', '$2a$12$PKx8f4xFPUOGvcCYXDungeW4iqByNyYCV2PgSCkZkz3XLYrW6xI8C', '5326-3333'),
       ('Mars', 'IamGod@gmail.com', '$2a$12$U5nrpEChgIZ/zJyxGoR/juULoBeMjXQ0BbuRpo5UC40dGGSzQHq/C', '5326-4444'),
       ('Jupiter', 'jupiter@gmail.com', '$2a$12$nqtuS5pztUC5KQCmkfSVrOz2/29AOkhtvRo2aA2tvaAtaS.KxX9Pu', '5326-5555'),
       ('Saturn', 'saturn@gmail.com', '$2a$12$V7fUNIeTWXuaYI.h1y6vU.kw.XWDSLrr0CWAerz2CXOt68xw6hWY6', '5326-6666'),
       ('Uranus', 'uranus@gmail.com', '$2a$12$QeFpNeLiciQYzUStxnLz9.nIaZLQCPmJDVqJs7dtynP8nqb.2mzgC', '5326-7777'),
       ('Neptune', 'neptune@gmail.com', '$2a$12$v6NE3b9CwU.D09ZaJDhw9uVPH3zmCj6Pv3qVBB0iv7NPDmnS71Nca', '5326-8888');

INSERT INTO employee (name, email, password, department)
VALUES ('Phobos', 'phobos@gmail.com', '$2a$12$pUy28qlPDaGVaV74OWDZ8ufFJvbfZrdR87fKroY1DRPhFrdOVHxcq', 'salle'),
       ('Moon', 'moon@gmail.com', '$2a$12$VnIPr7BnHhPqo5ZHuPxC5e.RnqcL1m03rB1WHtN67VB/CXd22B7Hi', 'salle'),
       ('Deimos', 'deimos@gmail.com', '$2a$12$PKx8f4xFPUOGvcCYXDungeW4iqByNyYCV2PgSCkZkz3XLYrW6xI8C', 'security'),
       ('Europa', 'europa@gmail.com', '$2a$12$U5nrpEChgIZ/zJyxGoR/juULoBeMjXQ0BbuRpo5UC40dGGSzQHq/C', 'security'),
       ('Ganymede', 'ganymede@gmail.com', '$2a$12$nqtuS5pztUC5KQCmkfSVrOz2/29AOkhtvRo2aA2tvaAtaS.KxX9Pu', 'engineering'),
       ('Callisto', 'callisto@gmail.com', '$2a$12$V7fUNIeTWXuaYI.h1y6vU.kw.XWDSLrr0CWAerz2CXOt68xw6hWY6', 'engineering'),
       ('Io', 'io@gmail.com', '$2a$12$QeFpNeLiciQYzUStxnLz9.nIaZLQCPmJDVqJs7dtynP8nqb.2mzgC', 'research');

INSERT INTO appliance (name, category, model, manufacturer_id, power_type, characteristic, description, power, price)
VALUES ('Claw', 'BIG', '-----', 1, 'ACCUMULATOR', '-------', '-----', 600, 1.01),
       ('Bane', 'SMALL', '-----', 3, 'AC110', '--------', '-----', 2200, 2.01),
       ('Ecu', 'SMALL', '-----', 2, 'ACCUMULATOR', '--------', '-----', 800, 3.01),
       ('Kang Dae', 'BIG', '-----', 4, 'AC220', '--------', '-----', 3600, 4.01),
       ('Gust', 'BIG', '-----', 5, 'ACCUMULATOR', '--------', '-----', 650, 5.01),
       ('Ancile', 'SMALL', '-----', 6, 'AC220', '--------', '-----', 230, 6.01),
       ('Halo', 'BIG', '-----', 7, 'ACCUMULATOR', '--------', '-----', 300, 7.01),
       ('Blaze', 'BIG', '-----', 7, 'AC110', '--------', '-----', 1500, 8.01),
       ('Storm', 'BIG', '-----', 7, 'AC220', '--------', '-----', 1800, 9.01),
       ('Zephyr', 'SMALL', '-----', 7, 'ACCUMULATOR', '--------', '-----', 500, 10.01),
       ('Cyclone', 'BIG', '-----', 7, 'AC110', '--------', '-----', 2500, 11.01),
       ('Tempest', 'BIG', '-----', 7, 'AC220', '--------', '-----', 2000, 12.01),
       ('Vortex', 'SMALL', '-----', 7, 'ACCUMULATOR', '--------', '-----', 700, 13.01);

COMMIT;
