package org.PayMyBuddy.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;
    @NotNull
    @Column(name = "user_email", unique = true)
    private String email;
    @Column(name = "user_password")
    private String password;

    @NotNull
    @Column(name = "user_firstname")
    private String firstname;
    @NotNull
    @Column(name = "user_lastname")
    private String lastname;

    @NotNull
    @Column(name = "user_role")
    private String role;

    @Min(value = 0, message = "Balance must be equal to or greater than zero")
    @Column(name = "user_balance", columnDefinition = "decimal(38,2) default 0")
    private BigDecimal balance = BigDecimal.ZERO;




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    public String getFirstname() { return firstname;}
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname;}
    public void setLastname(String lastname) { this.lastname = lastname; }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



}
