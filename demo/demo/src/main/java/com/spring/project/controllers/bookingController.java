package com.spring.project.controllers;

import java.security.Principal;
import java.util.List;

import com.spring.project.models.movies.Movie;
import com.spring.project.models.movies.movieservices.MovieServices;
import com.spring.project.models.movies.movieservices.MoviesRepository;
import com.spring.project.models.shows.Show;
import com.spring.project.models.shows.showservices.ShowRepository;

import com.spring.project.models.users.User;
import com.spring.project.models.users.userinfo.CardInfo;
import com.spring.project.models.users.userservices.CardInfoRepository;
import com.spring.project.models.users.userservices.UserRepository;
import com.spring.project.services.EncryptFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class bookingController {
    @Autowired
    private MovieServices movieServices; // Use MovieServices to fetch movie details

    @Autowired
    private ShowRepository showRepo;

    @Autowired
    private MoviesRepository repo; // Reference to movie repository interface

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CardInfoRepository cardRepo;

    private EncryptFacade encrypt = EncryptFacade.getInstance();

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

        return "selectshow";
    }
    @GetMapping("/checkout")
    public String checkout(CardInfo cardInfo, Principal principal, Model model) throws Exception {
        if(principal == null) {return "homepage";}

        User user = userRepo.findByEmail(principal.getName());
        List<CardInfo> cards = user.getPaymentInfo();

        for(int i = 0; i < cards.size(); i++) {
            if(!cards.get(i).getCardNumber().equals("")) {

                String decCardNum = encrypt.decryptCard(cards.get(i));
                String cardLastFour = decCardNum.substring(decCardNum.length() - 4);
                cardLastFour = "**** **** **** " + cardLastFour;
                cards.get(i).setCardNumber(cardLastFour);
            } // if
        } // for
        model.addAttribute("cards", cards);
        return "checkout";
    } // checkout


}
