package com.spring.project.controllers;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.spring.project.models.Movie;
import com.spring.project.models.MovieDTO;
import com.spring.project.services.MovieServices;
import com.spring.project.services.MoviesRepository;
import com.spring.project.services.UserRepository;
import com.spring.project.services.PromoRepository;
import com.spring.project.services.PromotionService;
import org.springframework.stereotype.Controller;

import com.spring.project.models.Promotion;
import com.spring.project.users.*;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class adminController {

    @Autowired
    private MoviesRepository repo; // Reference to movie repository interface

    @Autowired
    private UserRepository userRepo; // Reference to user repository interface

    @Autowired
    private MovieServices movieService; // Reference to movie services interface

    @Autowired
    private PromoRepository promoRepository;
    @Autowired
    private PromotionService promotionService;
    
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



    // Promotions
    @GetMapping("/admin/promos")
    public String promos(Model model) {
        List<Promotion> promotions = promoRepository.findAll();
        model.addAttribute("promotions", promotions);
        return "promos"; // Ensure this matches the name of your Thymeleaf template
    }

    @GetMapping("/admin/promos/delete/{promotionId}")
    public String deletePromotion(@PathVariable Long promotionId, RedirectAttributes redirectAttributes) {
        Promotion promotion = promoRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid promotion Id:" + promotionId));
        if (promotion.isSent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Promotion already sent and cannot be deleted.");
        } else {
            promoRepository.delete(promotion);
            redirectAttributes.addFlashAttribute("successMessage", "Promotion deleted successfully.");
        }
        return "redirect:/admin/promos";
    }

    @DeleteMapping("/admin/promos/delete/{promotionId}")
    public ResponseEntity<?> deletePromotion(@PathVariable Long promotionId) {
        Optional<Promotion> promotionOptional = promoRepository.findById(promotionId);
        if (promotionOptional.isPresent()) {
            Promotion promotion = promotionOptional.get();
            if (!promotion.isSent()) {
                promoRepository.delete(promotion);
                return ResponseEntity.ok().build(); // Successfully deleted
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Promotion has been sent and cannot be deleted.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Promotion not found.");
        }
    }
    // Method to display the form for creating a new promotion
    @GetMapping("/admin/promos/new")
    public String showCreatePromotionForm(Model model) {
        model.addAttribute("promotion", new Promotion()); // Add a blank promotion object to the model
        return "createPromotionForm"; // Name of the Thymeleaf template for the create promotion form
    }

    // Method to process the form submission
    @PostMapping("/admin/promos/create")
    public String createPromotion(@ModelAttribute Promotion promotion, RedirectAttributes redirectAttributes) {
        promotionService.createNewPromotion(promotion);
        redirectAttributes.addFlashAttribute("successMessage", "Promotion created successfully!");
        return "redirect:/admin/promos";
    }

    @GetMapping("/admin/promos/send/{promotionId}")
    public String sendPromotion(@PathVariable Long promotionId, RedirectAttributes redirectAttributes) throws MessagingException, UnsupportedEncodingException {
        Promotion promotion = promoRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid promotion Id:" + promotionId));
        if (promotion.isSent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Promotion already sent.");
        } else {
            // Call a method in your service to send the promotion to all subscribed users
            promotionService.sendPromotionToSubscribedUsers(promotionId);

            // Mark the promotion as sent and save
            promotion.setSent(true);
            promoRepository.save(promotion);

            redirectAttributes.addFlashAttribute("successMessage", "Promotion sent successfully.");
        }
        return "redirect:/admin/promos";
    }

    @GetMapping("/admin/promos/check-sent/{promotionId}")
    @ResponseBody
    public Map<String, Boolean> checkPromotionSent(@PathVariable Long promotionId) {
        Promotion promotion = promoRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid promotion Id:" + promotionId));
        Map<String, Boolean> response = new HashMap<>();
        response.put("sent", promotion.isSent());
        return response;
    }
    @GetMapping("/admin/promos/can-delete/{promotionId}")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> canDeletePromotion(@PathVariable Long promotionId) {
        Optional<Promotion> promotionOptional = promoRepository.findById(promotionId);
        Map<String, Boolean> response = new HashMap<>();
        if (promotionOptional.isPresent()) {
            Promotion promotion = promotionOptional.get();
            response.put("canDelete", !promotion.isSent());
            return ResponseEntity.ok(response);
        } else {
            response.put("canDelete", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    } // Promotions
} // homepageController
