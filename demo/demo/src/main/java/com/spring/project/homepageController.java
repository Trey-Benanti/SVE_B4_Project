package com.spring.project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class homepageController {

    @GetMapping("/")
    public String homepage() {
        return "homepage";
    } // homepage
} // homepageController
