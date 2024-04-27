package com.spring.project.models.users;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Column(name = "verCode", nullable = false)
    public String verCode;

    @Column(name = "user_type", nullable = false)
    public Role role = Role.ROLE_CUSTOMER;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id", nullable = true)
    private Address address;

    @Column(name = "subscription", nullable = false)
    private boolean subscription;

    @OneToMany(
            mappedBy = "userId",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Column(name = "payment_info", nullable = true)
    private List<CardInfo> paymentInfo = new ArrayList<>();
   // public List<CardInfo> paymentInfo = new ArrayList<CardInfo>(Arrays.asList(new CardInfo(), new CardInfo(), new CardInfo()));



    // Setters

    public void setPaymentInfo(List<CardInfo> paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

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

    public void setRole(Role r) {
        this.role = r;
    }

    public void setSubscription(boolean tf) {
        this.subscription = tf;
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

    public Role getRole() {
        return this.role;
    }

    public Address getAddress() {
        return this.address;
    }

    public boolean getSubscription() {
        return this.subscription;
    }

    public String getVerCode() {
        return this.verCode;
    }

    public List<CardInfo> getPaymentInfo() {
        return paymentInfo;
    }
}
