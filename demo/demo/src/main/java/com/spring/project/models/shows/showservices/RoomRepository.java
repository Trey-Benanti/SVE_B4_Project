package com.spring.project.models.shows.showservices;

import com.spring.project.models.shows.Show;
import com.spring.project.models.shows.showinfo.Showroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Showroom, Long> {

    @Query(value = "SELECT r FROM Showroom r WHERE r.id = ?1")
    public Showroom findByNumber(Long id);

}
