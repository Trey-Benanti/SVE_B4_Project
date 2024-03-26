package com.spring.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class loginController {
        
    @GetMapping("/login")
    public String login() {
        return "login";
    } // signin

}
