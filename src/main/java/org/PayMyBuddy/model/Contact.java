package org.PayMyBuddy.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "contact")
public class Contact {

    @EmbeddedId
    private ContactKey id;

    public ContactKey getId() {
        return id;
    }

    public void setId(ContactKey id) {
        this.id = id;
    }
}