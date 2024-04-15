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
    public Movie movie;

    @ManyToOne
    @JoinColumn(name = "room_id",nullable = false)
    public Showroom room;

    @Column(name = "period")
    public int period;

    @Column(name = "show_date")
    public Date showDate;
}
