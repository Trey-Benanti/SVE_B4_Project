package com.spring.project.models;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
     
    @Column(name = "email")
    public String email;
     
    @Column(name = "user_password")
    public String password;
     
    @Column(name = "first_name")
    public String firstName;
     
    @Column(name = "last_name")
    public String lastName;

    @Column(name = "orderType")
    public UserType type = UserType.CUSTOMER;

    // All setters lns 26-40
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

    public void promote() {
        this.type = UserType.ADMIN;
    }

    public void demote() {
        this.type = UserType.CUSTOMER;
    }

    // All getters lns 43-61
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

}
