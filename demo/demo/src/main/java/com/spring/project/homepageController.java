package com.spring.project;

import com.spring.project.models.Movie;
import com.spring.project.models.MovieDTO;
import com.spring.project.models.User;
import com.spring.project.models.UserRepository;
import com.spring.project.models.SecurityController;
import com.spring.project.services.MovieServices;
import com.spring.project.services.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class homepageController {

    @Autowired
    private MoviesRepository repo; // Reference to movie repository interface

    @Autowired
    private UserRepository userRepo; // Reference to user repository interface

    @Autowired
    private MovieServices movieService; // Reference to movie services interface

    @GetMapping("")
    public String homepage(Model model) { // Render homepage
        List<Movie> movies = repo.findAll();
        model.addAttribute("movies", movies);
        return "index";
    } // homepage

    @GetMapping("/home")
    public String customerHome(Model model) { // Render homepage
        List<Movie> movies = repo.findAll();
        model.addAttribute("movies", movies);
        return "homepage";
    } // homepage

    @GetMapping("/confirm")
    public String confirm() { return "confirmation";} // confirm

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

    @GetMapping("/promos")
    public String promos() { return "promos"; } // promos

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

    @GetMapping("/select-show")
    public String selectShow() {
        return "select-show";
    } // selectShow

    @GetMapping("/signin")
    public String signin() {
        return "signin";
    } // signin

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    } // signup

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    
        userRepo.save(user);
    
        return "register_success";
    }
    

    @GetMapping("/confirmation")
    public String confirmation() {
        return "confirmation";
    } // confirmation



} // homepageController

