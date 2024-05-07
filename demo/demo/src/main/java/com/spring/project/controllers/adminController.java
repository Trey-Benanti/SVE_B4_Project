package com.spring.project.controllers;

import com.spring.project.models.movies.Movie;
import com.spring.project.models.movies.movieinfo.MovieDTO;
import com.spring.project.models.movies.movieservices.MovieServices;
import com.spring.project.models.movies.movieservices.MoviesRepository;
import com.spring.project.models.promos.Promotion;
import com.spring.project.models.promos.promotionservices.PromoRepository;
import com.spring.project.models.promos.promotionservices.PromotionService;
import com.spring.project.models.shows.Show;
import com.spring.project.models.shows.showinfo.Seat;
import com.spring.project.models.shows.showinfo.Showroom;
import com.spring.project.models.shows.showservices.RoomRepository;
import com.spring.project.models.shows.showservices.SeatRepository;
import com.spring.project.models.shows.showservices.ShowRepository;
import com.spring.project.models.users.User;
import com.spring.project.models.users.userinfo.Role;
import com.spring.project.models.users.userinfo.UserStatus;
import com.spring.project.models.users.userservices.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


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

    @Autowired
    private PromoRepository promoRepository;
    @Autowired
    private PromotionService promotionService;

    @Autowired
    private SeatRepository seatRepository;

    @GetMapping("/admin/")
    public String adminView(Model model) {
        List<Movie> movies = repo.findAll();
        model.addAttribute("movies", movies);
        return "adminView";
    } // adminView

    @GetMapping("/admin/managemovies")
    public String manageMovies(Model model) {
        List<Movie> movies = repo.findAll();
        model.addAttribute("movies", movies);
        return "managemovies";
    }

    @GetMapping("/admin/editmovies")
    public String editMovies(Model model) {
        List<Movie> movies = repo.findAll();
        model.addAttribute("movies", movies);
        return "editmovies";
    } // editMovies
    @GetMapping("/admin/editmovie/{id}")
    public String showEditMovieForm(@PathVariable("id") int id, Model model) {
        Movie movie = movieService.findById(id);
        if (movie != null) {
            model.addAttribute("movie", movie);
            return "editmovieform";
        }
        return "redirect:/admin/managemovies"; // Redirect if movie not found
    }
    // Controller method to handle the update
    @PostMapping("/admin/updateMovie/{id}")
    public String updateMovie(@PathVariable("id") Integer id, @Valid @ModelAttribute("movie") Movie movie, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "editmovie";
        }
        movie.setId(id);
        repo.save(movie);
        redirectAttributes.addFlashAttribute("success", "Movie updated successfully!");
        return "redirect:/admin/editmovies";
    }


    @GetMapping("/admin/addschedule") // addschedule get
    public String addSchedule(Model model) {
        List<Show> shows = showRepo.findAll();
        model.addAttribute("shows", shows);
        List<Movie> listMovies = repo.findAll();
        model.addAttribute("listMovies", listMovies);
        return "addschedule";
    } // addschedule

    @PostMapping("/admin/addschedule") // addschedule post
    public String addShowtime(
            @RequestParam("title") String title,
            @RequestParam("room") Long room,
            @RequestParam("showDate") String showDate,
            @RequestParam("timeslot") String timeslot,
            Model model
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
        if (existing_show.size() > 0) {
            List<Show> shows = showRepo.findAll();
            model.addAttribute("shows", shows);
            List<Movie> listMovies = repo.findAll();
            model.addAttribute("listMovies", listMovies);
            model.addAttribute("BAD_SCHEDULE", "Error: Conflict with existing scheduling");
            return "addschedule";
        }

        showRepo.save(show);

        char row = 'A';
        for (int i = 0; i < show.room_id.numSeats; i++) {
            Seat seat = new Seat();
            seat.setShowId(show);
            seat.setSeatNum(i);
            seat.setSeatStatus(0);

            StringBuilder label = new StringBuilder();
            label.append(row);
            label.append((i % 7) + 1);
            seat.setSeatLabel(label.toString());

            seatRepository.save(seat);

            if ((i + 1) % 7 == 0) {
                row++;
            }
        }

        show.movie_id.setNowPlaying("Now Showing");
        repo.save(show.movie_id);

        return "redirect:/admin/addschedule";
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
        movie.setCategory(movieDTO.getCategory());
        movie.setAdultTicket(movieDTO.getAdultTicket());
        movie.setChildTicket(movieDTO.getChildTicket());
        movie.setSeniorTicket(movieDTO.getSeniorTicket());

        repo.save(movie);

        return "redirect:/admin/"; // redirect home
    } // create



    @GetMapping("/admin/promos")
    public String promos(Model model) {
        List<Promotion> promotions = promoRepository.findAll();
        model.addAttribute("promotions", promotions);
        return "promos";
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
        return "createPromotionForm";
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

    @PostMapping("/admin/alterUserRole")
    public String alterUserRole(@RequestParam("userId") Long userId, Principal principal, Model model) {
        User currentUser = userRepo.findByEmail(principal.getName());
        User user = userRepo.retrieveUserByID(userId);

        if (user == null) {
            List<User> listUsers = userRepo.findAll();
            model.addAttribute("listUsers", listUsers);
            model.addAttribute("role_invalid_id", "User Not Found");
            return "manageusers";
        }

        if (user != currentUser) {
            if (user.getRole() == Role.ROLE_ADMIN) {
                user.setRole(Role.ROLE_CUSTOMER);
            } else if (user.getRole() == Role.ROLE_CUSTOMER) {
                user.setRole(Role.ROLE_ADMIN);
            } // if
        } else { // if
            List<User> listUsers = userRepo.findAll();
            model.addAttribute("listUsers", listUsers);
            model.addAttribute("role_edit_self", "Cannot edit self");
            return "manageusers";
        }

        userRepo.save(user);

        return "redirect:/admin/manageusers";
    }

    @PostMapping("/admin/alterUserStatus")
    public String alterUserStatus(@RequestParam("userId") Long userId, Principal principal, Model model) {
        User currentUser = userRepo.findByEmail(principal.getName());
        User user = userRepo.retrieveUserByID(userId);

        if (user == null) {
            List<User> listUsers = userRepo.findAll();
            model.addAttribute("listUsers", listUsers);
            model.addAttribute("status_invalid_id", "User Not Found");
            return "manageusers";
        }

        if (user != currentUser) {
            if (user.getStatus() == UserStatus.STATUS_ACTIVE) {
                user.setStatus(UserStatus.STATUS_SUSPENDED);
            } else if (user.getStatus() == UserStatus.STATUS_SUSPENDED) {
                user.setStatus(UserStatus.STATUS_ACTIVE);
            } // if
        } else {
            List<User> listUsers = userRepo.findAll();
            model.addAttribute("listUsers", listUsers);
            model.addAttribute("status_edit_self", "Cannot edit self");
            return "manageusers";
        } // if

        userRepo.save(user);

        return "redirect:/admin/manageusers";
    }




} // homepageController
