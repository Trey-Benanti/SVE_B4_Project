package com.spring.project.services;

import com.spring.project.models.Show;
import com.spring.project.models.Showroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Showroom, Long> {

    @Query(value = "SELECT r FROM Showroom r WHERE r.id = ?1")
    public Showroom findByNumber(Long id);

}
