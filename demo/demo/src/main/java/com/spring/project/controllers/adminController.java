package com.spring.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.spring.project.models.Movie;
import com.spring.project.models.MovieDTO;
import com.spring.project.services.MovieServices;
import com.spring.project.services.MoviesRepository;
import com.spring.project.services.UserRepository;
import com.spring.project.users.*;

import jakarta.validation.Valid;

public class adminController {

    @Autowired
    private MoviesRepository repo; // Reference to movie repository interface

    @Autowired
    private UserRepository userRepo; // Reference to user repository interface

    @Autowired
    private MovieServices movieService; // Reference to movie services interface
    
    @GetMapping("/admin/")
    public String adminView(Model model) {
        List<Movie> movies = repo.findAll();
        model.addAttribute("movies", movies);
        return "adminView";
    } // adminView

    @GetMapping("/admin/managemovies")
    public String manageMovies() {
        return "manageMovies";
    } // manageMovies

    @GetMapping("/admin/editmovies")
    public String editMovies(Model model) {
        List<Movie> movies = repo.findAll();
        model.addAttribute("movies", movies);
        return "editmovies";
    } // editMovies

    @GetMapping("/admin/addschedule")
    public String addSchedule() {
        return "addschedule";
    } // addschedule

    @GetMapping("/admin/manageusers")
    public String manageUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers); 
        return "manageusers";
    } // manageusers

    @GetMapping("/admin/create") // create get
    public String create(Model model) { // Render create page
        MovieDTO movieDTO = new MovieDTO();
        model.addAttribute("movieDTO", movieDTO);
        return "create";
    } // create

    @PostMapping("/admin/create") // create post
    public String createMovie(
            @Valid @ModelAttribute MovieDTO movieDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {return "/admin/create";} // Return to create if form input error

        // Post form input to DB
        Movie movie = new Movie();
        movie.setMovieTitle(movieDTO.getMovieTitle());
        movie.setCast(movieDTO.getCast());
        movie.setDirector(movieDTO.getDirector());
        movie.setProducer(movieDTO.getProducer());
        movie.setRating(movieDTO.getRating());
        movie.setReviews(movieDTO.getReviews());
        movie.setSynop(movieDTO.getSynop());
        movie.setTrailerPhoto(movie.getTrailerPhoto());
        movie.setTrailerVideo(movieDTO.getTrailerVideo());
        movie.setNowPlaying(movieDTO.getNowPlaying());

        repo.save(movie);

        return "redirect:/admin/"; // redirect home
    } // create

        @GetMapping("/search")
    public String search(@Param("keyword") String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Search results for " + keyword);

        List<Movie> searchResult = movieService.search(keyword); // Get results of full text search
        model.addAttribute("searchResult", searchResult); // Pass search results to front end

        return "searchresult";
    } // search


    @GetMapping("/scheduling")
    public String scheduling(Model model) {
        List<Movie> movies = repo.findAll();
        model.addAttribute("movies", movies);
        return "scheduling";
    } // scheduling

    @GetMapping("/manageusers")
    public String manageUsers() {
        return "manageusers";
    } // manageusers

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    } // checkout

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

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    } // profile

    @GetMapping("/editprofile")
    public String editprofile() {
        return "editprofile";
    } // editprofile
    @GetMapping("/creditcards")
    public String creditcards() {
        return "creditcards";
    } // creditcards

    @GetMapping("/editcards")
    public String editcards() {
        return "editcards";
    } // editcards

    @GetMapping("/orders")
    public String prders() {
        return "orders";
    } // orders
    @GetMapping("/select-show")
    public String selectShow() {
        return "select-show";
    } // selectShow

    @GetMapping("/signin")
    public String signin() {
        return "signin";
    } // signin

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    } // signup

    @GetMapping("/confirmation")
    public String confirmation() {
        return "confirmation";
    } // confirmation





    @GetMapping("/admin/promos")
    public String promos() { return "promos"; } // promos

} // homepageController
