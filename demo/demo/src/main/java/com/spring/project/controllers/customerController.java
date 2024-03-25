package com.spring.project.controllers;

import com.spring.project.services.UserRepository;
import com.spring.project.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class customerController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    } // profile

    @GetMapping("/editprofile")
    public String editprofile(Model model, Principal principal) {
        model.addAttribute("user", userRepo.findByEmail(principal.getName()));
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
    public String orders() {
        return "orders";
    } // orders

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

} // homepageController

