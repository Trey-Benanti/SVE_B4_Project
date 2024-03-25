package com.spring.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.spring.project.models.Movie;
import com.spring.project.services.MoviesRepository;

@Controller
public class bookingController {

    @Autowired
    private MoviesRepository repo; // Reference to movie repository interface

    @GetMapping("/scheduling")
    public String scheduling(Model model) {
        List<Movie> movies = repo.findAll();
        model.addAttribute("movies", movies);
        return "scheduling";
    } // scheduling

    @GetMapping("/select-show")
    public String selectShow() {
        return "select-show";
    } // selectShow

    @GetMapping("/order-confirmation")
    public String orderConfirmation() {
        return "order-confirmation";
    } // orderConfirmation

    @GetMapping("/order-summary")
    public String orderSummary() {
        return "order-summary";
    } // orderSummary

    @GetMapping("/select-seats")
    public String selectSeats() {
        return "select-seats";
    } // selectSeats

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    } // checkout
    
}
