package org.PayMyBuddy.constant;

public class SqlQueries {

    //classe SqlQueries.class dans laquelle on met toutes les requetes SQL en tant que public static final String
    public static final String allContactsByUser = "SELECT * FROM contact WHERE contact_first_user_id = ?1 OR contact_second_user_id = ?1";
    public static final String allTransactionsBySenderAndReceiver = "SELECT t FROM Transaction t WHERE t.sender = ?1 OR t.receiver = ?2";
}