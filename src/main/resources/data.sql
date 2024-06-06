-- Insertion de données dans la table user
INSERT INTO user (user_id, user_email, user_password, user_firstname, user_lastname, user_role, user_balance) VALUES
(1, 'user@example.com', '$2y$10$dp.FpiGkFeb0WeA.VstKhuOZo2c79yO5eXwlk.YODQpfYc4ObM0EC', 'User', 'X', 'USER', 200.00),
(2, 'clara@example.com', '$2y$10$hhCrJNIRPrjSJtu1avH8euh/mWEw7cF4uO1sX6Vz4LfkLgNBTOB6W', 'Clara', 'X', 'USER', 0.00);

-- Insertion de données dans la table contact
INSERT INTO contact (contact_first_user_id, contact_second_user_id) VALUES
(1, 2);


-- Insertion de données dans la table transaction
INSERT INTO transaction (transaction_sender, transaction_receiver, transaction_amount, transaction_description) VALUES
(1, 2, 10.00, 'Payment for services');


