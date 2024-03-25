package com.spring.project.users;

import jakarta.persistence.*;

import java.util.Collection;

import com.spring.project.users.userinfo.*;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     
    @Column(name = "email", nullable = false, unique = true)
    private String email;
     
    @Column(name = "user_password", nullable = false)
    private String password;
     
    @Column(name = "first_name", nullable = false)
    private String firstName;
     
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "verCode")
    private String verCode;

    @OneToMany
    @JoinTable( 
        name = "user_type", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id")) 
    private Role role;

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

    public void setRole

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

    public Role getType() {
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
