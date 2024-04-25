package com.spring.project.controllers;

import java.util.List;

import com.spring.project.models.Show;
import com.spring.project.services.MovieServices;
import com.spring.project.services.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.spring.project.models.Movie;
import com.spring.project.services.MoviesRepository;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class bookingController {
    @Autowired
    private MovieServices movieServices; // Use MovieServices to fetch movie details

    @Autowired
    private ShowRepository showRepo;

    @Autowired
    private MoviesRepository repo; // Reference to movie repository interface

    @GetMapping("/select-show")
    public String selectShow() {
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
    public String selectShow(@PathVariable("id") int id, Model model) {
        Movie movie = repo.getReferenceById(id);

        List<Show> shows = showRepo.findByMovie(movie.getId());

        model.addAttribute("movie", movie);
        model.addAttribute("shows", shows);

        return "selectshow";
    }
    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    } // checkout

}
