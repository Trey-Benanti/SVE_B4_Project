package com.spring.project.controllers;

import java.util.List;

import com.spring.project.services.MovieServices;
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
    private MovieServices movieService; // Reference to movie services interface

    @Autowired
    private MoviesRepository repo; // Reference to movie repository interface

    @GetMapping("")
    public String homepage(Model model) { // Render homepage
        List<Movie> movies = repo.findAll();
        model.addAttribute("movies", movies);
        return "homepage";
    } // homepage

    @GetMapping("/search")
    public String search(@Param("keyword") String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Search results for " + keyword);

        List<Movie> searchResult = movieService.search(keyword); // Get results of full text search
        model.addAttribute("searchResult", searchResult); // Pass search results to front end

        return "searchresult";
    } // search

    @GetMapping("/filter")
    public String filter(@Param("keyword") String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        return "filter";
    }
    
}
