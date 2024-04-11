CREATE DATABASE IF NOT EXISTS paymybuddy_db;
USE paymybuddy_db;

CREATE TABLE IF NOT EXISTS dbuser (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100),
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    balance DECIMAL(10, 2)
);

CREATE TABLE IF NOT EXISTS contact (
    connection_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_email VARCHAR(100) NOT NULL,
    contact_email VARCHAR(100) NOT NULL,
    starting_date DATETIME NOT NULL,
    FOREIGN KEY (user_email)
        REFERENCES dbuser (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (contact_email)
        REFERENCES dbuser (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS transaction (
    transaction_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fk_issuer_id INT NOT NULL,
    fk_payee_id INT NOT NULL,
    date DATETIME NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    description VARCHAR(140),
    FOREIGN KEY (fk_issuer_id)
        REFERENCES dbuser (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (fk_payee_id)
        REFERENCES dbuser (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);
