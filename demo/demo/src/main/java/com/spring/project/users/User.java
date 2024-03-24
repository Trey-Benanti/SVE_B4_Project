package com.spring.project.users;

import jakarta.persistence.*;
import com.spring.project.users.userinfo.*;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
     
    @Column(name = "email", nullable = false, unique = true)
    public String email;
     
    @Column(name = "user_password", nullable = false)
    public String password;
     
    @Column(name = "first_name", nullable = false)
    public String firstName;
     
    @Column(name = "last_name", nullable = false)
    public String lastName;

    @Column(name = "verCode")
    public String verCode;

    @Column(name = "user_type", nullable = false)
    public UserType type = UserType.CUSTOMER;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id", nullable = true)
    private Address address;

    @Column(name = "subscription", nullable = false)
    private boolean subscribed;

    // Setters

    public void setEmail(String mail) {
        this.email = mail;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    public void setFirstName(String first) {
        this.firstName = first;
    }

    public void setLastName(String last) {
        this.lastName = last;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void promote() {
        this.type = UserType.ADMIN;
    }

    public void demote() {
        this.type = UserType.CUSTOMER;
    }

    public void subscribe() {
        this.subscribed = true;
    }

    public void unsubscribe() {
        this.subscribed = false;
    }

    public void setVerCode(String code) {
        this.verCode = code;
    }

    // Getters

    public String getID() {
        return this.id.toString();
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public UserType getType() {
        return this.type;
    }

    public Address getAddress() {
        return this.address;
    }

    public boolean getSubscription() {
        return this.subscribed;
    }

    public String getVerCode() {
        return this.verCode;
    }
}
