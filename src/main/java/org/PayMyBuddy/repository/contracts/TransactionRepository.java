
package org.PayMyBuddy.repository.contracts;

import java.util.List;

import org.PayMyBuddy.constant.SqlQueries;
import org.PayMyBuddy.model.Transaction;
import org.PayMyBuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = SqlQueries.allTransactionsBySenderAndReceiver)
    List<Transaction> findBySenderOrReceiver(User sender, User receiver);
}
