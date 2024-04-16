package com.spring.project.services;

import com.spring.project.models.Show;
import com.spring.project.models.Showroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer> {

  //  @Query(value = "SELECT s FROM Show s WHERE s.room_id = ?1 AND s.show_date = ?2 AND s.time_slot = ?3", nativeQuery = true)
    //public Show findByTimeSlot(Long room_id, Date date, String slot);

}
