package com.spring.project.controllers;

import java.util.List;

import com.spring.project.models.movies.Movie;
import com.spring.project.models.movies.movieservices.MovieServices;
import com.spring.project.models.movies.movieservices.MoviesRepository;
import com.spring.project.models.shows.Show;
import com.spring.project.models.shows.showinfo.Seat;
import com.spring.project.models.shows.showservices.SeatRepository;
import com.spring.project.models.shows.showservices.ShowRepository;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class bookingController {
    @Autowired
    private MovieServices movieServices; // Use MovieServices to fetch movie details

    @Autowired
    private ShowRepository showRepo;

    @Autowired
    private MoviesRepository repo; // Reference to movie repository interface

    @Autowired
    private SeatRepository seatRepo;

    @GetMapping("/select-show")
    public String selectShow() {
        return "select-show";
    } // selectShow
    @GetMapping("/select-seats")
    public String selectSeats() {
        return "select-seats";
    } // selectSeats

    @GetMapping("/order-confirmation")
    public String orderConfirmation() {
        return "order-confirmation";
    } // orderConfirmation

    @GetMapping("/order-summary")
    public String orderSummary() {
        return "order-summary";
    } // orderSummary

    @GetMapping("/select-show/{id}")
    public String selectShow(@PathVariable("id") int id, Model model) {
        Movie movie = repo.getReferenceById(id);

        List<Show> shows = showRepo.findByMovie(movie.getId());

        model.addAttribute("movie", movie);
        model.addAttribute("shows", shows);

        return "select-show";
    }

    @GetMapping("/select-seats/{id}")
    public String selectSeats(@PathVariable("id") int id, Model model) {

        List<Show> showList = showRepo.findById(id);
        Show show = showList.get(0);
        Movie movie = show.movie_id;

        model.addAttribute("movie", movie);

        String seatsGrid = drawSeats(show);
        model.addAttribute("seatsGrid", seatsGrid);

        return "select-seats";
    }
    public String drawSeats(Show show) {
        List<Seat> seats = seatRepo.seatsInShow(show.id);

        StringBuilder result = new StringBuilder();
        char row = 'A';

        int count = 0;
        for (int i = 0; i < (seats.size() + 6) / 7; i++) {
            result.append("<div style='display: flex;'>");
            for (int j = 0; j < 7 && count < seats.size(); j++) {
                result.append("<div style='width: 50px; height: 50px; background-color: ");
                result.append(seats.get(count).getSeatStatus() == 1 ? "red" : "white");
                result.append("; display: flex; justify-content: center; align-items: center;'>");
                result.append(row).append(j + 1); // Label format: rowLetter + columnNumber
                result.append("</div>");
                count++;
            }
            result.append("</div>");
            row++;
        }

        return result.toString();
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    } // checkout

}
