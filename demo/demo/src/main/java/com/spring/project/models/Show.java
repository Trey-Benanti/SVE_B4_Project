package com.spring.project.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="showtimes")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    public Movie movie_id;

    @ManyToOne
    @JoinColumn(name = "room_id",nullable = false)
    public Showroom room_id;

    @Column(name = "show_date")
    public String showDate;

    @Column(name = "time_slot")
    public String time_slot;
}
