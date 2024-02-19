package com.spring.project;

import com.spring.project.models.Movie;
import com.spring.project.models.MovieDTO;
import com.spring.project.services.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;

import java.util.List;

@Controller
public class homepageController {

    @Autowired
    private MoviesRepository repo;

    @GetMapping("/")
    public String homepage(Model model) { // Render homepage
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

        repo.save(movie);

        return "redirect:/"; // redirect home
    }

    @GetMapping("/adminview")
    public String adminView() {
        return "adminView";
    } // adminView

    @GetMapping("/managemovies")
    public String manageMovies() {
        return "manageMovies";
    } // manageMovies

    @GetMapping("/promos")
    public String promos() { return "promos"; } // promos

} // homepageController

