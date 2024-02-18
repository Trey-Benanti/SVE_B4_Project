package com.spring.project;

import com.spring.project.models.Movie;
import com.spring.project.services.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.util.List;

@Controller
public class homepageController {

    @GetMapping("/")
    public String homepage() {
        return "homepage";
    } // homepage

    @GetMapping("/confirm")
    public String confirm() { return "confirmation";} // confirm

    @Autowired
    private MoviesRepository repo;

    @GetMapping("/temp")
    public String temp(Model model) {
        List<Movie> movies = repo.findAll();
        model.addAttribute("movies", movies);
        return "temp";
    } // temp



} // homepageController

