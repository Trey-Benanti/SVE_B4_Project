package com.spring.project.models.bookings.bookingservices;

import com.spring.project.models.bookings.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query(value = "SELECT * FROM bookings WHERE user_id = (?!)", nativeQuery = true)
    List<Booking> findBookingByUser(int user_id);

    @Query(value = "SELECT * FROM bookings WHERE confirmed != 1", nativeQuery = true)
    List<Booking> findUnconfirmedBookings();

}
