package com.spring.project.controllers;

import com.spring.project.models.bookings.Booking;
import com.spring.project.models.bookings.bookingservices.BookingRepository;
import com.spring.project.models.tickets.Ticket;
import com.spring.project.models.tickets.ticketservices.TicketRepository;
import com.spring.project.models.users.User;
import com.spring.project.models.users.userinfo.CardInfo;
import com.spring.project.models.users.userservices.*;
import com.spring.project.services.EncryptFacade;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.*;
import java.util.List;

@Controller
public class customerController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CardInfoRepository cardRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private TicketRepository ticketRepo;

    private EncryptFacade encrypt = EncryptFacade.getInstance();

    @GetMapping("/profile")
    public String profile(Model model, Principal principal, HttpSession session) {
        if (principal == null)
        {
            return "homepage";
        }

        List<Booking> unconfirmed = bookingRepo.findUnconfirmedBookings();
        for(int i = 0; i < unconfirmed.size(); i++) {
            List<Ticket> unconTickets = ticketRepo.findByBookingId(unconfirmed.get(i).getId());
            for (int j = 0; j < unconTickets.size(); j++) {
                ticketRepo.delete(unconTickets.get(j));
            }
            bookingRepo.delete(unconfirmed.get(i));
        }
        Booking booking = (Booking) session.getAttribute("booking");

        model.addAttribute("user", userRepo.findByEmail(principal.getName()));
        return "profile";
    } // profile

    @GetMapping("/editprofile")
    public String editprofile(Model model, Principal principal) {
        if (principal == null)
        {
            return "homepage";
        }
        model.addAttribute("user", userRepo.findByEmail(principal.getName()));
        return "editprofile";
    } // editprofile

    @GetMapping("/creditcards")
    public String creditcards(Principal principal, Model model) throws Exception {
        if (principal == null)
        {
            return "homepage";
        }

        // Copying off Sam here O_o
        User edited = userRepo.findByEmail(principal.getName());
        List<CardInfo> cards = edited.getPaymentInfo(); // Gets each list of card info

        for(int i = 0; i < cards.size(); i++) { // Decrypt each card number
            if(!cards.get(i).getCardNumber().equals("")) {

                String decCardNum = encrypt.decryptCard(cards.get(i));
                String cardLastFour = decCardNum.substring(decCardNum.length() - 4);
                cardLastFour = "**** **** **** " + cardLastFour;
                cards.get(i).setCardNumber(cardLastFour); // Set model card to decrypted card number
            } // if
        } // for

        model.addAttribute("cards", cards);
        return "creditcards";
    } // creditcards

    @GetMapping("/editcards/{id}")
    public String editcards(@PathVariable Long id, Model model) throws Exception {
        CardInfo card = cardRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Card ID"));

        if (!card.getCardNumber().equals("")) {

            String decCardNum = encrypt.decryptCard(card);
            card.setCardNumber(decCardNum);
        } // if

        model.addAttribute("card", card);
        return "editcards";
    } // editcards

    @PostMapping("/editcards")
    public String saveCard(@ModelAttribute CardInfo card, Principal principal) throws Exception {
        User user = userRepo.findByEmail(principal.getName());
        int i = 0;
        boolean found = false;
        while (!found) {
            if (user.getPaymentInfo().get(i).getId() == card.getId()) {
                found = true;
            } else {
                i++;
            }
        }
        CardInfo editedCard = cardRepo.findById(card.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid Card ID"));
        editedCard.setId(card.getId());
        editedCard.setCardName(card.getCardName());
        editedCard.setExpirationDate(card.getExpirationDate());
        editedCard.setSecurityCode(card.getSecurityCode());
        editedCard.setUserId(user);
        editedCard.setCardNumber(card.getCardNumber());
        editedCard.setBillingAddrStreet(card.getBillingAddrStreet());
        editedCard.setBillingAddrState(card.getBillingAddrState());
        editedCard.setBillingAddrZip(card.getBillingAddrZip());

        String toEnc = editedCard.getCardNumber(); // Encrypt card number
        editedCard.setCardNumber(encrypt.encryptCard(user, toEnc, i));

        cardRepo.save(editedCard);
        return "redirect:/creditcards";
    } // saveCard

    @GetMapping("/orders")
    public String orders(HttpSession session) {
        List<Booking> unconfirmed = bookingRepo.findUnconfirmedBookings();
        for(int i = 0; i < unconfirmed.size(); i++) {
            List<Ticket> unconTickets = ticketRepo.findByBookingId(unconfirmed.get(i).getId());
            for (int j = 0; j < unconTickets.size(); j++) {
                ticketRepo.delete(unconTickets.get(j));
            }
            bookingRepo.delete(unconfirmed.get(i));
        }
        Booking booking = (Booking) session.getAttribute("booking");

        return "orders";
    } // orders

    @GetMapping("/changepassword")
    public String changepassword() {
        return "changepassword";
    }

    @PostMapping("/make_edits")
    public String make_edits(User user, Principal principal) {

        User edited = userRepo.findByEmail(principal.getName());

        edited.setFirstName(user.getFirstName());
        edited.setLastName(user.getLastName());
        edited.setAddress(user.getAddress());
        edited.setSubscription(user.getSubscription());

        userRepo.save(edited);

        return "profile";

    }

    @PostMapping("/changepw")
    public String changepw(@RequestParam("currPass") String currPass,
                           @RequestParam("newPass") String newPass,
                           @RequestParam("confirmPass") String confirmPass,
                           Model model,
                           Principal principal) {

        User user = userRepo.findByEmail(principal.getName());

        System.out.println(user.getPassword());
        if (!encrypt.compareToPassword(user, currPass)) {
            model.addAttribute("wrong_pass", "Incorrect Password");
            return "changepassword";
        }

        if (!newPass.equals(confirmPass)) {
            model.addAttribute("no_match", "Passwords do not match");
            return "changepassword";
        }

        encrypt.encryptPassword(user, newPass);

        userRepo.save(user);

        return "redirect:/";
    }


} // homepageController

