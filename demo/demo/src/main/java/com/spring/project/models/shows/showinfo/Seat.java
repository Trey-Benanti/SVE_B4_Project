package com.spring.project.models.shows.showinfo;

import com.spring.project.models.shows.Show;
import jakarta.persistence.*;

@Entity
@Table(name="seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private Show show_id;

    @Column(name = "seat_status")
    private int seat_status;

    @Column(name = "seat_num")
    private int seat_num;

    @Column(name = "seat_label")
    private String seat_label;


    public int getId() {
        return id;
    }

    public Show getShowId() {
        return show_id;
    }

    public void setShowId(Show show_id) {
        this.show_id = show_id;
    }

    public int getSeatStatus() {
        return seat_status;
    }

    public void setSeatStatus(int seat_status) {
        this.seat_status = seat_status;
    }

    public int getSeatNum() {
        return seat_num;
    }

    public void setSeatNum(int seat_num) {
        this.seat_num = seat_num;
    }

    public String getSeatLabel() {
        return seat_label;
    }

    public void setSeatLabel(String seat_label) {
        this.seat_label = seat_label;
    }
}
