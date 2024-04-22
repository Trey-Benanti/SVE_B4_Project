package com.spring.project.controllers;

import java.util.List;

import com.spring.project.services.MovieServices;
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
    public String selectShow(@PathVariable("id") int movieId, Model model) {
        Movie movie = movieServices.findById(movieId);
        model.addAttribute("movie", movie);
        return "select-show";
    }
    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    } // checkout

}
