package com.spring.project.models.shows.showservices;

import com.spring.project.models.shows.Show;
import com.spring.project.models.shows.showinfo.Showroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer> {

    @Query(value = "SELECT * FROM showtimes  WHERE room_id = :room AND show_date = :date AND time_slot = :slot", nativeQuery = true)
    public List<Show> findByTimeSlot(@Param("room") Long room_id, @Param("date") String date, @Param("slot") String slot);

    @Query(value = "SELECT * FROM showtimes WHERE movie_id = (?1)", nativeQuery = true)
    public List<Show> findByMovie(int movie_id);

    @Query(value = "SELECT * FROM showtimes WHERE id=(?1)", nativeQuery = true)
    public List<Show> findById(int id);

}
