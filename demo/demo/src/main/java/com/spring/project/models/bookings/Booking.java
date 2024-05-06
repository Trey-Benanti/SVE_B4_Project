package com.spring.project.models.bookings;

import com.spring.project.models.promos.Promotion;
import com.spring.project.models.shows.Show;
import com.spring.project.models.users.User;
import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "bookingNum")
    private int bookingNum;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "showId")
    private Show show;

    @ManyToOne
    @JoinColumn(name = "promoId")
    private Promotion promo;

    @Column(name = "totalCost")
    private double totalCost;

    @Column(name = "confirmed")
    private int confirmed;

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }
} // Booking
