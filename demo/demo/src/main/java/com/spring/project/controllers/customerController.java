package com.spring.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class customerController {

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    } // profile
    
    @GetMapping("/editprofile")
    public String editprofile() {
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
} // homepageController

