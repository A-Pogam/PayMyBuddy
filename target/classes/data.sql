USE `paymybuddy_db `;

INSERT INTO dbuser (id, email, password, role) VALUES
(1, 'user@example.com', '$2y$10$.qkbukzzX21D.bqbI.B2R.tvWP90o/Y16QRWVLodw51BHft7ZWbc.', 'USER'),
(2, 'admin@example.com', '$2y$10$kp1V7UYDEWn17WSK16UcmOnFd1mPFVF6UkLrOOCGtf24HOYt8p1iC', 'ADMIN');

INSERT INTO `connection` (`connection_id`, `fk_initializer_id`, `fk_receiver_id`, `starting_date`) VALUES

INSERT INTO `transaction` (transactionId, senderId, receiverId, date, amount, description, receiverFirstName, receiverLastName) VALUES
