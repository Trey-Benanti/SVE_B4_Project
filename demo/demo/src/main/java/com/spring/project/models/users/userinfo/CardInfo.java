package com.spring.project.models.users.userinfo;

import com.spring.project.models.users.*;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_info")
public class CardInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User userId;

    @Column(name = "cardNum", nullable = false)
    private String cardNum;
    
    @Column(name = "cardName", nullable = false)
    private String cardName;
    
    @Column(name = "expDate", nullable = false)
    private String expirationDate;

    @Column(name = "secCode", nullable = false)
    private String securityCode;

    @Column(name = "billingAddrStreet", nullable = false)
    private String billingAddrStreet;

    @Column(name = "billingAddrState", nullable = false)
    private String billingAddrState;

    @Column(name = "billingAddrZip", nullable = false)
    private String billingAddrZip;

    public CardInfo(User userId, String cardNum, String cardName, String expirationDate, String securityCode,
                    String billingAddrStreet, String billingAddrState, String billingAddrZip) {
        this.userId = userId;
        this.cardNum = cardNum;
        this.cardName = cardName;
        this.expirationDate = expirationDate;
        this.securityCode = securityCode;
        this.billingAddrStreet = billingAddrStreet;
        this.billingAddrState = billingAddrState;
        this.billingAddrZip = billingAddrZip;
    }

    public CardInfo() {}

    // Setters


    public void setUserId(User userId) {
        this.userId = userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCardNumber(String num) {
        this.cardNum = num;
    }

    public void setCardName(String name) {
        this.cardName = name;
    }

    public void setExpirationDate(String date) {
        this.expirationDate = date;
    }

    public void setSecurityCode(String code) {
        this.securityCode = code;
    }

    public void setBillingAddrStreet(String billingAddrStreet) {
        this.billingAddrStreet = billingAddrStreet;
    }

    public void setBillingAddrState(String billingAddrState) {
        this.billingAddrState = billingAddrState;
    }

    public void setBillingAddrZip(String billingAddrZip) {
        this.billingAddrZip = billingAddrZip;
    }

    // Getters

    public User getUserId() {
        return this.userId;
    }

    public Long getId() {
        return id;
    }

    public String getCardNumber() {
        return this.cardNum;
    }
    public String getCardName() {
        return this.cardName;
    }
    public String getExpirationDate() {
        return this.expirationDate;
    }
    public String getSecurityCode() {
        return this.securityCode;
    }

    public String getBillingAddrState() {
        return billingAddrState;
    }

    public String getBillingAddrStreet() {
        return billingAddrStreet;
    }

    public String getBillingAddrZip() {
        return billingAddrZip;
    }
}
