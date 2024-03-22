package com.spring.project.users;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "street_address", nullable = false)
    public String streetAddress;

    @Column(name = "apartment_num", nullable = true)
    public String appartNum;

    @Column(name = "city", nullable = false)
    public String cityName;

    @Column(name = "state_code", nullable = false)
    public StateCode stateCode;
    
    @Column(name = "zip_code", nullable = false)
    public Long zip;

}
