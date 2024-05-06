package com.spring.project.models.shows.showservices;

import com.spring.project.models.shows.showinfo.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Integer> {

    @Query(value = "SELECT * FROM seats WHERE show_id = :show AND seat_status = 0", nativeQuery = true)
    public List<Seat> findFreeShowSeats(@Param("show") int show_id);

    @Query(value = "SELECT * FROM seats WHERE show_id = (?1)", nativeQuery = true)
    public List<Seat> seatsInShow(int show_id);
}
