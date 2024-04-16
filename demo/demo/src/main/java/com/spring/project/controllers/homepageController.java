package com.spring.project.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import com.spring.project.models.*;
import com.spring.project.services.*;
import com.spring.project.users.User;
import com.spring.project.users.userinfo.Role;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
public class homepageController {
    @Autowired
    private MoviesRepository repo; // Reference to movie repository interface

    @Autowired
    private UserRepository userRepo; // Reference to user repository interface

    @Autowired
    private ShowRepository showRepo; // Reference to showtimes repository interface

    @Autowired
    private RoomRepository roomRepo; // Reference to showrooms repository interface

    @Autowired
    private MovieServices movieService; // Reference to movie services interface

    @GetMapping("")
    public String homepage(Model model, Principal principal) { // Render homepage
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

    @GetMapping("/selectshow/{id}")
    public String selectshow(@PathVariable int id, Model model) {
        Movie movie = repo.getReferenceById(id);
        model.addAttribute("movie", movie);

        return "selectshow";
    }

}

