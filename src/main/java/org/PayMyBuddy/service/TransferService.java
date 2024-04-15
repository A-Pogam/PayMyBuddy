package org.PayMyBuddy.service;

import org.PayMyBuddy.model.DBUser;
import org.PayMyBuddy.model.Transaction;
import org.PayMyBuddy.repository.DBUserRepository;
import org.PayMyBuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

    @Autowired
    private DBUserRepository dbUserRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public void transferMoney(Integer transaction_id, Integer sender_id, Integer receiver_id, String description, LocalDateTime date, BigDecimal amount, String receiver_first_name, String receiver_last_name) {
        // Récupérer l'utilisateur expéditeur de la base de données
        Optional<DBUser> senderOptional = dbUserRepository.findById(sender_id);
        if (senderOptional.isEmpty()) {
            // Gérer le cas où l'utilisateur expéditeur n'est pas trouvé
            throw new RuntimeException("Sender not found");
        }
        DBUser sender = senderOptional.get();

        // Récupérer l'utilisateur destinataire de la base de données
        Optional<DBUser> receiverOptional = dbUserRepository.findById(receiver_id);
        if (receiverOptional.isEmpty()) {
            // Gérer le cas où l'utilisateur destinataire n'est pas trouvé
            throw new RuntimeException("Receiver not found");
        }
        DBUser receiver = receiverOptional.get();

        // Vérifier si l'expéditeur a suffisamment de fonds pour effectuer la transaction
        BigDecimal senderBalance = sender.getBalance();
        if (senderBalance.compareTo(amount) < 0) {
            // Gérer le cas où l'expéditeur n'a pas suffisamment de fonds
            throw new RuntimeException("Insufficient funds");
        }

        // Déduire le montant de la balance de l'expéditeur
        BigDecimal newSenderBalance = senderBalance.subtract(amount);
        sender.setBalance(newSenderBalance);
        dbUserRepository.save(sender);

        // Mettre à jour la balance du destinataire
        BigDecimal receiverBalance = receiver.getBalance();
        BigDecimal newReceiverBalance = receiverBalance.add(amount);
        receiver.setBalance(newReceiverBalance);
        dbUserRepository.save(receiver);

        // Enregistrer la transaction dans la base de données
        Transaction transaction = new Transaction();
        transaction.setTransaction_id(transaction_id);
        transaction.setSender_id(sender_id);
        transaction.setReceiver_id(receiver_id);
        transaction.setDescription(description);
        transaction.setDate(date);
        transaction.setAmount(amount);
        transaction.setReceiver_first_name(receiver_first_name);
        transaction.setReceiver_last_name(receiver_last_name);
        transactionRepository.save(transaction);
    }


    public List<Transaction> getUserTransactions(Long userId) {
        // Récupérez les transactions de l'utilisateur à partir de la base de données
        Integer userIdAsInteger = userId.intValue();
        return transactionRepository.findBySenderIdOrReceiverId(userIdAsInteger, userIdAsInteger);
    }

}
