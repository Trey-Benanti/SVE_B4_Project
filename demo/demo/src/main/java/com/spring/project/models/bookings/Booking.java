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

    public double getTotalCost() {
        return this.totalCost;
    }

    @Column(name = "tax")
    private double tax;

    @Column(name = "grossTotal")
    private double grossTotal;

    // Getters and setters
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

    public Promotion getPromo() {
        return promo;
    }

    public void setPromo(Promotion promo) {
        this.promo = promo;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(double grossTotal) {
        this.grossTotal = grossTotal;
    }
} // Booking
