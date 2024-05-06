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
import com.spring.project.models.users.userinfo.Role;
import com.spring.project.models.users.userservices.UserRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class homepageController {
    @Autowired
    private MoviesRepository repo; // Reference to movie repository interface

    @Autowired
    private UserRepository userRepo; // Reference to user repository interface

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private TicketRepository ticketRepo;

    @Autowired
    private MovieServices movieService; // Reference to movie services interface

    @GetMapping("")
    public String homepage(Model model, Principal principal, HttpSession session) { // Render homepage
        List<Booking> unconfirmed = bookingRepo.findUnconfirmedBookings();
        for(int i = 0; i < unconfirmed.size(); i++) {
            List<Ticket> unconTickets = ticketRepo.findByBookingId(unconfirmed.get(i).getId());
            for (int j = 0; j < unconTickets.size(); j++) {
                ticketRepo.delete(unconTickets.get(j));
            }
            bookingRepo.delete(unconfirmed.get(i));
        }
        Booking booking = (Booking) session.getAttribute("booking");


        List<Movie> movies = repo.findAll();
        model.addAttribute("movies", movies);

        if (principal != null && userRepo.findByEmail(principal.getName()).getRole() == Role.ROLE_ADMIN)
        {
            return "adminView";
        }

        return "homepage";
    } // homepage

    @GetMapping("/search")
    public String search(@Param("keyword") String keyword, Model model) {
        List<Movie> searchResult = movieService.search(keyword);

        if(searchResult.isEmpty()) {
            keyword = "No results matching " + keyword;
        } else {
            keyword = "Results for " + keyword;
        } // else

        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", keyword);
        model.addAttribute("searchResult", searchResult); // Pass search results to front end
        return "searchresult";
    } // search

}

