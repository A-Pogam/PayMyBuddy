package org.PayMyBuddy.service;

import org.PayMyBuddy.model.DBUser;
import org.PayMyBuddy.model.Transaction;
import org.PayMyBuddy.repository.DBUserRepository;
import org.PayMyBuddy.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

    @Autowired
    private DBUserRepository dbUserRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public void transferMoney(Integer sender_id, Integer receiver_id, String description, BigDecimal amount) {
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
        updateReceiverBalance(receiver, amount);

        // Enregistrer la transaction dans la base de données
        Transaction transaction = new Transaction();
        transaction.setSender_id(sender_id);
        transaction.setReceiver_id(receiver_id);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
    }

    private void updateReceiverBalance(DBUser receiver, BigDecimal amount) {
        BigDecimal receiverBalance = receiver.getBalance();
        if (receiverBalance == null) {
            receiverBalance = BigDecimal.ZERO; // Initialiser la balance à zéro si elle est nulle
        }
        BigDecimal newReceiverBalance = receiverBalance.add(amount);
        receiver.setBalance(newReceiverBalance);
        dbUserRepository.save(receiver);
    }


    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findBySenderIdOrReceiverId(userId.intValue(), userId.intValue());
    }



}
