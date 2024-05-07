package com.spring.project.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.spring.project.models.Ticket;
import com.spring.project.models.TicketRepository;
import com.spring.project.models.bookings.Booking;
import com.spring.project.models.bookings.bookingservices.BookingRepository;
import com.spring.project.models.movies.Movie;
import com.spring.project.models.movies.movieservices.MovieServices;
import com.spring.project.models.movies.movieservices.MoviesRepository;
import com.spring.project.models.promos.Promotion;
import com.spring.project.models.promos.promotionservices.PromoRepository;
import com.spring.project.models.shows.Show;
import com.spring.project.models.shows.showinfo.Seat;
import com.spring.project.models.shows.showservices.SeatRepository;
import com.spring.project.models.shows.showservices.ShowRepository;

import com.spring.project.models.users.User;
import com.spring.project.models.users.userinfo.CardInfo;
import com.spring.project.models.users.userservices.UserRepository;
import com.spring.project.services.EncryptFacade;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static java.lang.Math.round;

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

    @Autowired
    private PromoRepository promoRepo;

    @Autowired
    private JavaMailSender mailSender;

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
                result.append("<div style='width: 25px; height: 25px; padding: 5px; margin:5px; background-color: ");

                boolean isInBooking = false;
                for (int k = 0; k < ticketsInBooking.size(); k++) {
                    if (ticketsInBooking.get(k).getSeatId() == seats.get(count)) {
                        isInBooking = true;
                    }
                }

                if (seats.get(count).getSeatStatus() == 1) {
                    result.append("rgb(206,98,67)"); // already booked
                } else if (isInBooking) {
                    result.append("rgb(221,160,57)"); // currently picked
                } else {
                    result.append("rgb(155,182,189)"); // not picked
                }
  
                result.append("; display: flex; justify-content: center; align-items: center;'>");
                result.append(row).append(j + 1); // visual Label format: rowLetter + columnNumber
                result.append("</div>");
                count++;
            }
            result.append("</div>");
            row++;
        }
        result.append("<div style='width: 90%; height: 15px; padding: 5px; margin:5px; background-color: rgb(155,182,189); display: flex; justify-content: center; align-items: center;'>");
        result.append("screen</div>");

        return result.toString();
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session, Principal principal) throws Exception {
        if(principal == null) {return "homepage";}

        EncryptFacade encrypt = EncryptFacade.getInstance();

        Booking booking = (Booking) session.getAttribute("booking"); // Get booking reference
        List<Ticket> tickets = ticketRepo.findByBookingId(booking.getId()); // Get tickets for booking
        List<Seat> seats = new ArrayList<>();

        for(int i = 0; i < tickets.size(); i++) { // Add all seats from booking to seats
            seats.addAll(seatRepo.getSeatById(tickets.get(i).getSeatId().getId()));
        } // for

        Show show = booking.getShow(); // Get reference to show
        Movie movie = movieServices.findById(show.movie_id.getId()); // Get reference to movie

        String totalCost;
        if(booking.getTotalCost() == 0) {
            totalCost = calculateTotal(tickets); // Get total cost
        } else {
            totalCost = String.format("%.2f", booking.getTotalCost());
        }

        User user = userRepo.findByEmail(principal.getName());
        List<CardInfo> cards = user.getPaymentInfo();

        for(int i = 0; i < cards.size(); i++) { // Decrypt cards
            if(!cards.get(i).getCardNumber().equals("")) {

                String decCardNum = encrypt.decryptCard(cards.get(i));
                String cardLastFour = decCardNum.substring(decCardNum.length() - 4);
                cardLastFour = "**** **** **** " + cardLastFour;
                cards.get(i).setCardNumber(cardLastFour);
            } // if
        } // for
        model.addAttribute("cards", cards);
        model.addAttribute("seats", seats);
        model.addAttribute("tickets", tickets);
        model.addAttribute("movie", movie);
        model.addAttribute("show", show);
        model.addAttribute("total", totalCost);
        model.addAttribute("booking", booking);
        return "checkout";
    } // checkout

    @PostMapping("/apply-promo/")
    public String applyPromo(HttpSession session, @RequestParam("promoCode") String promoCode,
                             @RequestParam String totalCost) {
        Booking booking = (Booking) session.getAttribute("booking");
        List<Promotion> promoList = promoRepo.findAll();
        Promotion promo = null;

        if(booking.getPromo() != null) {return "redirect:/checkout";}

        for(int i = 0; i < promoList.size(); i++) {
            if(promoList.get(i).getPromoCode().equals(promoCode)) {
                promo = promoList.get(i);
            }
        } // for
        if(promo == null) {return "redirect: /checkout";}

        double cost = Double.valueOf(totalCost) - (Double.valueOf(totalCost) * (promo.getPercentage() * .01));
        booking.setTotalCost(cost);
        booking.setPromo(promo);
        bookingRepo.save(booking);

        return "redirect:/checkout";
    } // applyPromo

    @GetMapping("/order-confirmation")
    public String orderConfirmation(Model model, HttpSession session, Principal principal) throws Exception {
        if(principal == null) {return "homepage";}

        Booking booking = (Booking) session.getAttribute("booking");
        List<Ticket> tickets = ticketRepo.findByBookingId(booking.getId());
        List<Seat> seats = new ArrayList<>();

        for(int i = 0; i < tickets.size(); i++) { // Add all seats from booking to seats
            seats.addAll(seatRepo.getSeatById(tickets.get(i).getSeatId().getId()));
            seats.get(i).setSeatStatus(1); // Set seat as reserved
            seatRepo.save(seats.get(i));
        } // for

        Show show = booking.getShow();
        Movie movie = movieServices.findById(show.movie_id.getId());
        String totalCost = String.format("%.2f", booking.getTotalCost());

        booking.setConfirmed(1); // Set booking as confirmed
        bookingRepo.save(booking);

        User user = userRepo.findByEmail(principal.getName());

        sendConfirmEmail(user, seats, show, totalCost, movie);

        model.addAttribute("seats", seats);
        model.addAttribute("tickets", tickets);
        model.addAttribute("movie", movie);
        model.addAttribute("show", show);
        model.addAttribute("total", totalCost);
        model.addAttribute("booking", booking);
        return "order-confirmation";
    } // orderConfirmation

    private void sendConfirmEmail(User user, List<Seat> seatList, Show show,
                                  String totalCost, Movie movie) throws MessagingException, UnsupportedEncodingException {

        StringBuilder seatsBuilder = new StringBuilder(); // Construct seats string
        for(int i = 0; i < seatList.size(); i++) {
            seatsBuilder.append(seatList.get(i).getSeatLabel()).append(" ");
        } // for
        String seats = seatsBuilder.toString();

        // Send order confirmation email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("tidalwavetheaters@gmail.com", "Tidal Wave Theaters");
        helper.setTo(user.getEmail());
        helper.setSubject("Order Confirmation");

        String content = "<h1>Thank you for your order</h1>"
                + "<h3>Details</h3>"
                + "<p>" + "Seats: " + seats + "</p>"
                + "<p>" + movie.getMovieTitle() + "</p>"
                + "<p>" + "Rated " + movie.getRating() + "</p>"
                + "<p>" + show.room_id.roomTitle + "</p>"
                + "<p>" + show.showDate + "</p>"
                + "<p>" + show.time_slot + "</p>"
                + "<p>" + "Total Cost: $" + totalCost + "</p>";

        helper.setText(content, true);
        mailSender.send(message);
    } // sendConfirmEmail

    private String calculateTotal(List<Ticket> ticketList) {
        double total = 0.0;
        for(int i = 0; i < ticketList.size(); i++) {
            total += Double.valueOf(ticketList.get(i).getPrice());
        }
        return String.format("%.2f", total);
    } // calculateTotal

}
