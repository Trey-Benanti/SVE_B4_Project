package com.spring.project.models;

import com.spring.project.models.bookings.Booking;
import com.spring.project.models.shows.showinfo.Seat;
import jakarta.persistence.*;

@Entity
@Table(name="tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking_id;

    @OneToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat_id;

    @Column(name = "price")
    private int price;

}
