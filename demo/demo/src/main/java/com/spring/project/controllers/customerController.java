package com.spring.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class customerController {

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    } // profile

} // homepageController

