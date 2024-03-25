package com.spring.project.users.userinfo;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @Column(name = "apartment_num", nullable = true)
    private String apartmentNumber;

    @Column(name = "city", nullable = false)
    private String cityName;

    @Enumerated(EnumType.STRING)
    @Column(name = "state_code", nullable = false)
    private StateCode stateCode;
    
    @Column(name = "zip_code", nullable = false)
    private int zipCode;

    // Setters

    public void setStreetAddress(String address) {
        this.streetAddress = address;
    }

    public void setApartmentNumber(String apart) {
        this.apartmentNumber = apart;
    }

    public void setCityName(String city) {
        this.cityName = city;
    }

    public void setStateCode(StateCode code) {
        this.stateCode = code;
    }

    public void setZipCode(int z) {
        this.zipCode = z;
    }

    // Getters

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public String getApartmentNumber() {
        return this.apartmentNumber;
    }

    public String getCityName() {
        return this.cityName;
    }

    public StateCode getStateCode() {
        return this.stateCode;
    }

    public int getZipCode() {
        return this.zipCode;
    }

}
