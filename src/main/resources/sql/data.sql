-- DML

INSERT INTO manufacturer (name)
VALUES ('Samsung'),
       ('Dell'),
       ('HP'),
       ('Apple'),
       ('Lenovo'),
       ('Acer'),
       ('AMD');

ALTER TABLE client ALTER COLUMN role SET DEFAULT 'CLIENT';
INSERT INTO client (name, email, password, shipping_address, enabled)
VALUES ('Mercury', 'mercury@gmail.com', '$2a$12$pUy28qlPDaGVaV74OWDZ8ufFJvbfZrdR87fKroY1DRPhFrdOVHxcq', '123 Mercury St, Solar City, SC 12345', true),
       ('Venus', 'venus@gmail.com', '$2a$12$VnIPr7BnHhPqo5ZHuPxC5e.RnqcL1m03rB1WHtN67VB/CXd22B7Hi', '456 Venus Ave, Solar City, SC 12345', true),
       ('Earth', 'earth@gmail.com', '$2a$12$PKx8f4xFPUOGvcCYXDungeW4iqByNyYCV2PgSCkZkz3XLYrW6xI8C', '789 Earth Blvd, Solar City, SC 12345', true),
       ('Mars', 'IamGod@gmail.com', '$2a$12$U5nrpEChgIZ/zJyxGoR/juULoBeMjXQ0BbuRpo5UC40dGGSzQHq/C', '101 Mars Rd, Solar City, SC 12345', true),
       ('Jupiter', 'jupiter@gmail.com', '$2a$12$nqtuS5pztUC5KQCmkfSVrOz2/29AOkhtvRo2aA2tvaAtaS.KxX9Pu', '202 Jupiter Ln, Solar City, SC 12345', false),
       ('Saturn', 'saturn@gmail.com', '$2a$12$V7fUNIeTWXuaYI.h1y6vU.kw.XWDSLrr0CWAerz2CXOt68xw6hWY6', '303 Saturn Dr, Solar City, SC 12345', true),
       ('Uranus', 'uranus@gmail.com', '$2a$12$QeFpNeLiciQYzUStxnLz9.nIaZLQCPmJDVqJs7dtynP8nqb.2mzgC', '404 Uranus Ct, Solar City, SC 12345', false),
       ('Neptune', 'neptune@gmail.com', '$2a$12$v6NE3b9CwU.D09ZaJDhw9uVPH3zmCj6Pv3qVBB0iv7NPDmnS71Nca', '505 Neptune Pl, Solar City, SC 12345', false);

ALTER TABLE employee ALTER COLUMN role SET DEFAULT 'EMPLOYEE';
INSERT INTO employee (name, email, password, department)
VALUES ('Phobos', 'phobos@gmail.com', '$2a$12$pUy28qlPDaGVaV74OWDZ8ufFJvbfZrdR87fKroY1DRPhFrdOVHxcq', 'salle'),
       ('Moon', 'moon@gmail.com', '$2a$12$VnIPr7BnHhPqo5ZHuPxC5e.RnqcL1m03rB1WHtN67VB/CXd22B7Hi', 'salle'),
       ('Deimos', 'deimos@gmail.com', '$2a$12$PKx8f4xFPUOGvcCYXDungeW4iqByNyYCV2PgSCkZkz3XLYrW6xI8C', 'security'),
       ('Europa', 'europa@gmail.com', '$2a$12$U5nrpEChgIZ/zJyxGoR/juULoBeMjXQ0BbuRpo5UC40dGGSzQHq/C', 'security'),
       ('Ganymede', 'ganymede@gmail.com', '$2a$12$nqtuS5pztUC5KQCmkfSVrOz2/29AOkhtvRo2aA2tvaAtaS.KxX9Pu', 'engineering'),
       ('Callisto', 'callisto@gmail.com', '$2a$12$V7fUNIeTWXuaYI.h1y6vU.kw.XWDSLrr0CWAerz2CXOt68xw6hWY6', 'engineering'),
       ('Io', 'io@gmail.com', '$2a$12$QeFpNeLiciQYzUStxnLz9.nIaZLQCPmJDVqJs7dtynP8nqb.2mzgC', 'research');

ALTER TABLE admin ALTER COLUMN role SET DEFAULT 'ADMIN';
INSERT INTO admin (name, email, password)
VALUES ('Admin', 'admin@gmail.com', '$2a$12$pUy28qlPDaGVaV74OWDZ8ufFJvbfZrdR87fKroY1DRPhFrdOVHxcq');

INSERT INTO appliance (name, category, model, manufacturer_id, power_type, characteristic, description, power, price)
VALUES ('Claw', 'BIG', 'CLW-1000', 1, 'ACCUMULATOR', 'High power claw machine', 'Used for heavy-duty tasks', 600, 1.01),
       ('Bane', 'SMALL', 'BNE-2000', 3, 'AC110', 'Compact and efficient', 'Ideal for small spaces', 2200, 2.01),
       ('Ecu', 'SMALL', 'ECU-3000', 2, 'ACCUMULATOR', 'Energy-efficient', 'Portable and lightweight', 800, 3.01),
       ('Kang Dae', 'BIG', 'KDG-4000', 4, 'AC220', 'High performance', 'Suitable for industrial use', 3600, 4.01),
       ('Gust', 'BIG', 'GST-5000', 5, 'ACCUMULATOR', 'Powerful and durable', 'Great for outdoor use', 650, 5.01),
       ('Ancile', 'SMALL', 'ANC-6000', 6, 'AC220', 'Reliable and safe', 'Perfect for home use', 230, 6.01),
       ('Halo', 'BIG', 'HLO-7000', 7, 'ACCUMULATOR', 'Versatile and robust', 'Handles tough jobs', 300, 7.01),
       ('Blaze', 'BIG', 'BLZ-8000', 7, 'AC110', 'Fast and efficient', 'High-speed operations', 1500, 8.01),
       ('Storm', 'BIG', 'STM-9000', 7, 'AC220', 'Heavy-duty', 'Designed for large projects', 1800, 9.01),
       ('Zephyr', 'SMALL', 'ZPH-10000', 7, 'ACCUMULATOR', 'Quiet and efficient', 'Ideal for indoor use', 500, 10.01),
       ('Cyclone', 'BIG', 'CYC-11000', 7, 'AC110', 'High power', 'Handles extreme conditions', 2500, 11.01),
       ('Tempest', 'BIG', 'TMP-12000', 7, 'AC220', 'Robust and reliable', 'Perfect for continuous use', 2000, 12.01),
       ('Vortex', 'SMALL', 'VTX-13000', 7, 'ACCUMULATOR', 'Compact and powerful', 'Great for quick tasks', 700, 13.01);


COMMIT;
