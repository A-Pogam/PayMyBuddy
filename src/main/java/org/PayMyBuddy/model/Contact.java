package org.PayMyBuddy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contact", uniqueConstraints = @UniqueConstraint(columnNames =  {"contact_first_user_id", "contact_second_user_id"}))
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contactId;

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