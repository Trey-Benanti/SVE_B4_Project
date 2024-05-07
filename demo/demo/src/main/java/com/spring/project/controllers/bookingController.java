package com.spring.project.controllers;

import java.security.Principal;
import java.util.List;

import com.spring.project.models.Ticket;
import com.spring.project.models.TicketRepository;
import com.spring.project.models.bookings.Booking;
import com.spring.project.models.bookings.bookingservices.BookingRepository;
import com.spring.project.models.movies.Movie;
import com.spring.project.models.movies.movieservices.MovieServices;
import com.spring.project.models.movies.movieservices.MoviesRepository;
import com.spring.project.models.shows.Show;
import com.spring.project.models.shows.showinfo.Seat;
import com.spring.project.models.shows.showservices.SeatRepository;
import com.spring.project.models.shows.showservices.ShowRepository;

import com.spring.project.models.users.userservices.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class bookingController {
    @Autowired
    private MovieServices movieServices; // Use MovieServices to fetch movie details

    @Autowired
    private ShowRepository showRepo;

    @Autowired
    private MoviesRepository repo; // Reference to movie repository interface

    @Autowired
    private SeatRepository seatRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TicketRepository ticketRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @GetMapping("/select-show")
    public String selectShow(HttpSession session) {

        List<Booking> unconfirmed = bookingRepo.findUnconfirmedBookings();
        for(int i = 0; i < unconfirmed.size(); i++) {
            List<Ticket> unconTickets = ticketRepo.findByBookingId(unconfirmed.get(i).getId());
            for (int j = 0; j < unconTickets.size(); j++) {
                ticketRepo.delete(unconTickets.get(j));
            }
            bookingRepo.delete(unconfirmed.get(i));
        }
        Booking booking = (Booking) session.getAttribute("booking");

        return "select-show";
    } // selectShow
    @GetMapping("/select-seats")
    public String selectSeats() {
        return "select-seats";
    } // selectSeats

    @GetMapping("/order-confirmation")
    public String orderConfirmation() {
        return "order-confirmation";
    } // orderConfirmation

    @GetMapping("/order-summary")
    public String orderSummary() {
        return "order-summary";
    } // orderSummary

    @GetMapping("/select-show/{id}")
    public String selectShow(@PathVariable("id") int id, Model model, Principal principal, HttpSession session) {
        List<Booking> unconfirmed = bookingRepo.findUnconfirmedBookings();
        for(int i = 0; i < unconfirmed.size(); i++) {
            List<Ticket> unconTickets = ticketRepo.findByBookingId(unconfirmed.get(i).getId());
            for (int j = 0; j < unconTickets.size(); j++) {
                ticketRepo.delete(unconTickets.get(j));
            }
            bookingRepo.delete(unconfirmed.get(i));
        }
        Booking booking = (Booking) session.getAttribute("booking");
        
        Movie movie = repo.getReferenceById(id);

        List<Show> shows = showRepo.findByMovie(movie.getId());

        model.addAttribute("movie", movie);
        model.addAttribute("shows", shows);

        return "select-show";
    }

    @GetMapping("/select-seats/{id}")
    public String selectSeats(@PathVariable("id") int id, 
    Model model, 
    HttpSession session, 
    Principal principal) {
        
        if (principal == null) {
            return "redirect:/login";
        }

        // Check if booking already exists in session
        Booking booking = (Booking) session.getAttribute("booking");

        // If booking doesn't exist, create a new one
        if (booking == null || ticketRepo.findByBookingId(booking.getId()).size() == 0) {
            booking = new Booking();
            Show show = showRepo.findById(id).get(0);
            booking.setShow(show);
            booking.setUser(userRepo.findByEmail(principal.getName()));
            booking.setConfirmed(0);
            session.setAttribute("booking", booking);
            bookingRepo.save(booking);
        }

        // Add movie and seats information to the model
        Show show = booking.getShow();
        Movie movie = show.movie_id;
        model.addAttribute("movie", movie);
        String seatsGrid = drawSeats(show, booking);
        model.addAttribute("seatsGrid", seatsGrid);

        List<Ticket> tickets = ticketRepo.findByBookingId(booking.getId());
        model.addAttribute("tickets", tickets);

        List<Seat> availableSeats = seatRepo.findFreeShowSeats(show.id);
        for (int i = 0; i < availableSeats.size(); i++) {
            for (int j = 0; j < tickets.size(); j++) {
                if (tickets.get(j).getSeatId() == availableSeats.get(i)) {
                    availableSeats.remove(i);
                }
            }
        }
        model.addAttribute("free_seats", availableSeats);

        model.addAttribute("show", booking.getShow());

        return "select-seats";
    }

    @PostMapping("/select-seats/{id}")
    public String selectSeats(
            @PathVariable("id") int id,
            @RequestParam("seat_pick") int seat_id,
            @RequestParam("age_range") String age_range,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Booking booking = (Booking) session.getAttribute("booking");

        Ticket ticket = new Ticket();
        Seat seat = seatRepo.getSeatById(seat_id).get(0);
        ticket.setSeatId(seat);
        ticket.setBooking(booking);

        if (age_range.equals("Adult")) {
            ticket.setPrice(seat.getShowId().movie_id.getAdultTicket());
        } else if (age_range.equals("Senior")) {
            ticket.setPrice(seat.getShowId().movie_id.getSeniorTicket());
        } else {
            ticket.setPrice(seat.getShowId().movie_id.getChildTicket());
        }

        ticketRepo.save(ticket);

        redirectAttributes.addAttribute("id", id);
        return "redirect:/select-seats/{id}";

    }

    @PostMapping("/delete-ticket/")
    public String deleteTicket(HttpSession session, RedirectAttributes redirectAttributes) {
        Booking booking = (Booking) session.getAttribute("booking");
        List<Ticket> tickets = ticketRepo.findByBookingId(booking.getId());
        ticketRepo.delete(tickets.get(tickets.size() - 1));

        redirectAttributes.addAttribute("id", booking.getShow().id);
        return "redirect:/select-seats/{id}";
    }

    public String drawSeats(Show show, Booking booking) {
        List<Seat> seats = seatRepo.seatsInShow(show.id);
        List<Ticket> ticketsInBooking = ticketRepo.findByBookingId(booking.getId());

        StringBuilder result = new StringBuilder();
        char row = 'A';

        int count = 0;
        for (int i = 0; i < (seats.size() + 6) / 7; i++) {
            result.append("<div style='display: flex;'>");
            for (int j = 0; j < 7 && count < seats.size(); j++) {
                result.append("<div style='width: 50px; height: 50px; background-color: ");

                boolean isInBooking = false;
                for (int k = 0; k < ticketsInBooking.size(); k++) {
                    if (ticketsInBooking.get(k).getSeatId() == seats.get(count)) {
                        isInBooking = true;
                    }
                }

                result.append(seats.get(count).getSeatStatus() == 1 || isInBooking ? "red" : "white");
                result.append("; display: flex; justify-content: center; align-items: center;'>");
                result.append(row).append(j + 1); // Label format: rowLetter + columnNumber
                result.append("</div>");
                count++;
            }
            result.append("</div>");
            row++;
        }

        return result.toString();
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    } // checkout

}
