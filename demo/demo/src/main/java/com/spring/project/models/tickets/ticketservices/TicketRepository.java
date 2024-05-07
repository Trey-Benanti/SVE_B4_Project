package com.spring.project.models.tickets.ticketservices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.project.models.tickets.Ticket;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query(value = "SELECT * FROM tickets WHERE booking_id = (?1)", nativeQuery = true)
    List<Ticket> findByBookingId(int booking_id);

}