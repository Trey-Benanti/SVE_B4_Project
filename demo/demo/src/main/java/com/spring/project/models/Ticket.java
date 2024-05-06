package com.spring.project.models;

import com.spring.project.models.bookings.Booking;
import com.spring.project.models.shows.showinfo.Seat;
import jakarta.persistence.*;

@Entity
@Table(name="tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @OneToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat_id;

    @Column(name = "price")
    private String price;

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Seat getSeatId() {
        return seat_id;
    }

    public void setSeatId(Seat seat_id) {
        this.seat_id = seat_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
