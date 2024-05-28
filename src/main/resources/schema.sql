-- paymybuddy_db.`user` definition

CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `user_balance` decimal(38,2) NOT NULL DEFAULT '0.00',
  `user_email` varchar(255) NOT NULL,
  `user_firstname` varchar(255) NOT NULL,
  `user_lastname` varchar(255) NOT NULL,
  `user_password` varchar(255) DEFAULT NULL,
  `user_role` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_j09k2v8lxofv2vecxu2hde9so` (`user_email`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- paymybuddy_db.contact definition

CREATE TABLE `contact` (
  `contact_id` int NOT NULL AUTO_INCREMENT,
  `contact_first_user_id` int DEFAULT NULL,
  `contact_second_user_id` int DEFAULT NULL,
  PRIMARY KEY (`contact_id`),
  KEY `FKdfadc9l6xviklj78wy7qj48cm` (`contact_first_user_id`),
  KEY `FK46wqa2dj44nyhks0dfdm068ll` (`contact_second_user_id`),
  CONSTRAINT `FK46wqa2dj44nyhks0dfdm068ll` FOREIGN KEY (`contact_second_user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKdfadc9l6xviklj78wy7qj48cm` FOREIGN KEY (`contact_first_user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- paymybuddy_db.`transaction` definition

CREATE TABLE `transaction` (
  `transaction_id` int NOT NULL AUTO_INCREMENT,
  `transaction_amount` decimal(38,2) NOT NULL,
  `transaction_description` varchar(255) DEFAULT NULL,
  `transaction_receiver` int NOT NULL,
  `transaction_sender` int NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `FK3a476bj9v7pchg16g1g8krqde` (`transaction_receiver`),
  KEY `FKmu13tpls41ea49ajiumnjbrkv` (`transaction_sender`),
  CONSTRAINT `FK3a476bj9v7pchg16g1g8krqde` FOREIGN KEY (`transaction_receiver`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKmu13tpls41ea49ajiumnjbrkv` FOREIGN KEY (`transaction_sender`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;