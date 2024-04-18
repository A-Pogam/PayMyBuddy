package org.PayMyBuddy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long connectionId;

    private Integer user_id;
    private String userEmail; // user who want to add a contact
    private String contactEmail; // new contact to add

    private String firstname;
    private String lastname;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }


    public String getFirstname() { return firstname;}
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname;}
    public void setLastname(String lastname) { this.lastname = lastname; }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
