package org.PayMyBuddy.model;

import java.io.Serializable;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class ContactKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne()
    @JoinColumn(name = "contact_first_user_id")
    private User firstUser;

    @ManyToOne()
    @JoinColumn(name = "contact_second_user_id")
    private User secondUser;

    public User getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(User firstUser) {
        this.firstUser = firstUser;
    }

    public User getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(User secondUser) {
        this.secondUser = secondUser;
    }
}