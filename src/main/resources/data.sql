-- Insertion de données dans la table user
INSERT INTO user (user_id, user_email, user_password, user_firstname, user_lastname, user_role, user_balance) VALUES
(1, 'user@example.com', 'user', 'User', 'X', 'USER', 200.00),
(2, 'clara@example.com', 'clara', 'Clara', 'X', 'USER', 0.00);

-- Insertion de données dans la table contact
INSERT INTO contact (contact_first_user_id, contact_second_user_id) VALUES
(1, 2);


-- Insertion de données dans la table transaction
INSERT INTO transaction (transaction_sender, transaction_receiver, transaction_amount, transaction_description) VALUES
(1, 2, 10.00, 'Payment for services');


