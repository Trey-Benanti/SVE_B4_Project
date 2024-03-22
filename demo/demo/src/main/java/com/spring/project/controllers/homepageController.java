package com.spring.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

import com.spring.project.models.Movie;
import com.spring.project.services.MoviesRepository;

@Controller
public class homepageController {

    @Autowired
    private MoviesRepository repo; // Reference to movie repository interface

    @GetMapping("")
    public String homepage(Model model) { // Render homepage
        List<Movie> movies = repo.findAll();
        model.addAttribute("movies", movies);
        return "homepage";
    } // homepage
    
}
