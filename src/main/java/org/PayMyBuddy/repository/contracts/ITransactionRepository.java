package org.PayMyBuddy.repository.contracts;

import org.PayMyBuddy.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.sender_id = :senderId OR t.receiver_id = :receiverId")
    List<Transaction> findBySenderIdOrReceiverId(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);
}