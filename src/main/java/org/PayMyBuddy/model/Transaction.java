package org.PayMyBuddy.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transaction_id;

    private Integer sender_id;
    private Integer receiver_id;
    private LocalDateTime date;
    private BigDecimal amount;
    private String description;
    private String receiver_first_name;
    private String receiver_last_name;



    public Integer getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(Integer transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Integer getSender_id() {
        return sender_id;
    }

    public void setSender_id(Integer sender_id) {
        this.sender_id = sender_id;
    }

    public Integer getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(Integer receiver_id) {
        this.receiver_id = receiver_id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiver_first_name() {
        return receiver_first_name;
    }

    public void setReceiver_first_name(String receiverFirstName) {
        this.receiver_first_name = receiver_first_name;
    }

    public String getReceiver_last_name() {
        return receiver_last_name;
    }

    public void setReceiver_last_name(String receiver_last_name) {
        this.receiver_last_name = receiver_last_name;
    }


}


