package com.spring.project.services;

import com.spring.project.models.Show;
import com.spring.project.models.Showroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Showroom, Integer> {

    @Query("SELECT u FROM Showroom u WHERE u.id = ?1")
    public Showroom findByNumber(int id);

}
