package com.spring.project.models.bookings.bookingservices;

import com.spring.project.models.bookings.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query(value = "SELECT * FROM bookings WHERE customer_id = (?1)", nativeQuery = true)
    List<Booking> findBookingByUser(int userId);

    @Query(value = "SELECT * FROM bookings WHERE confirmed != 1", nativeQuery = true)
    List<Booking> findUnconfirmedBookings();

    @Query(value = "SELECT * FROM bookings WHERE user_id = (?!) AND confirmed != 0", nativeQuery = true)
    List<Booking> confirmedBookingsByUser(Long user_id);

}
