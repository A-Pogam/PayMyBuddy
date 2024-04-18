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
            throw new RuntimeException("Sender not found");
        }
        DBUser sender = senderOptional.get();

        // Récupérer l'utilisateur destinataire de la base de données
        Optional<DBUser> receiverOptional = dbUserRepository.findById(receiver_id);
        if (receiverOptional.isEmpty()) {
            throw new RuntimeException("Receiver not found");
        }
        DBUser receiver = receiverOptional.get();

        // Vérifier si l'expéditeur a suffisamment de fonds pour effectuer la transaction
        BigDecimal senderBalance = sender.getBalance();
        if (senderBalance.compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // Calculer le montant du prélèvement (0,5 %)
        BigDecimal feePercentage = BigDecimal.valueOf(0.5);
        BigDecimal feeAmount = amount.multiply(feePercentage.divide(BigDecimal.valueOf(100)));

        // Calculer le montant final à transférer au destinataire (montant de la transaction - montant du prélèvement)
        BigDecimal finalAmount = amount.subtract(feeAmount);

        // Déduire le montant de la balance de l'expéditeur
        BigDecimal newSenderBalance = senderBalance.subtract(finalAmount); 
        sender.setBalance(newSenderBalance);
        dbUserRepository.save(sender);

        // Mettre à jour la balance du destinataire
        updateReceiverBalance(receiver, finalAmount);

        // Enregistrer la transaction dans la base de données
        Transaction transaction = new Transaction();
        transaction.setSender_id(sender_id);
        transaction.setReceiver_id(receiver_id);
        transaction.setDescription(description);
        transaction.setAmount(finalAmount);
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
