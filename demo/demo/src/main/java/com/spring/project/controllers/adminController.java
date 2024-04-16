package com.spring.project.controllers;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.spring.project.models.*;
import com.spring.project.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.spring.project.users.*;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class adminController {

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

    @GetMapping("/admin/addschedule") // addschedule get
    public String addSchedule(Model model) {
        List<Show> shows = showRepo.findAll();
        model.addAttribute("shows", shows);
        return "addschedule";
    } // addschedule

    @PostMapping("/admin/addschedule") // addschedule post
    public String addShowtime(
            @RequestParam("title") String title,
            @RequestParam("room") Long room,
            @RequestParam("showDate") String showDate,
            @RequestParam("timeslot") String timeslot
    ) {

        Show show = new Show();
        List<Movie> movieList = movieService.search(title);
        Showroom showroom = roomRepo.findByNumber(room);

        show.movie_id = movieList.get(0);
        show.room_id = showroom;
        show.time_slot = timeslot;
        show.showDate = showDate;

        // check if room is booked at this time
        List<Show> existing_show = showRepo.findByTimeSlot(show.room_id.id, show.showDate, show.time_slot);
        if (existing_show.size() > 0) { // TODO: add error text
            return "redirect:/admin/addschedule";
        }

        showRepo.save(show);

        show.movie_id.setNowPlaying("Now Showing");
        repo.save(show.movie_id);

        return "redirect:/admin/";
    }
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



    @GetMapping("/admin/promos")
    public String promos() { return "promos"; } // promos

} // homepageController
