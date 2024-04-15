package com.spring.project.models;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Showroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "num_seats")
    public int numSeats;

    @Column(name = "room_title")
    public String roomTitle;

}
