package com.spring.project.controllers;

import com.spring.project.services.CardInfoRepository;
import com.spring.project.services.UserRepository;
import com.spring.project.users.User;
import com.spring.project.users.userinfo.CardInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class customerController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        if (principal == null)
        {
            return "homepage";
        }

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
    public String creditcards(Principal principal, Model model) {
        if (principal == null)
        {
            return "homepage";
        }

        // Copying off Sam here O_o
        User edited = userRepo.findByEmail(principal.getName());
        List<CardInfo> cards = edited.getPaymentInfo(); // Gets each list of card info
        model.addAttribute("cards", cards);
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

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userRepo.findByEmail(principal.getName());

        System.out.println(user.getPassword());
        if (!passwordEncoder.matches(currPass, user.getPassword())) {
            model.addAttribute("wrong_pass", "Incorrect Password");
            return "changepassword";
        }

        if (!newPass.equals(confirmPass)) {
            model.addAttribute("no_match", "Passwords do not match");
            return "changepassword";
        }

        String encodedPassword = passwordEncoder.encode(newPass);
        user.setPassword(encodedPassword);

        userRepo.save(user);

        return "homepage";
    }


} // homepageController

