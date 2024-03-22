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
    
    @GetMapping("/adminview")
    public String adminView(Model model) {
        List<Movie> movies = repo.findAll();
        model.addAttribute("movies", movies);
        return "adminView";
    } // adminView

    @GetMapping("/managemovies")
    public String manageMovies() {
        return "manageMovies";
    } // manageMovies

    @GetMapping("/editmovies")
    public String editMovies(Model model) {
        List<Movie> movies = repo.findAll();
        model.addAttribute("movies", movies);
        return "editmovies";
    } // editMovies

    @GetMapping("/addschedule")
    public String addSchedule() {
        return "addschedule";
    } // addschedule

    @GetMapping("/manageusers")
    public String manageUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers); 
        return "manageusers";
    } // manageusers

    @GetMapping("/create") // create get
    public String create(Model model) { // Render create page
        MovieDTO movieDTO = new MovieDTO();
        model.addAttribute("movieDTO", movieDTO);
        return "create";
    } // create

    @PostMapping("/create") // create post
    public String createMovie(
            @Valid @ModelAttribute MovieDTO movieDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {return "/create";} // Return to create if form input error

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

        return "redirect:/"; // redirect home
    } // create

        @GetMapping("/search")
    public String search(@Param("keyword") String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Search results for " + keyword);

        List<Movie> searchResult = movieService.search(keyword); // Get results of full text search
        model.addAttribute("searchResult", searchResult); // Pass search results to front end

        return "searchresult";
    } // search

    @GetMapping("/promos")
    public String promos() { return "promos"; } // promos

}
