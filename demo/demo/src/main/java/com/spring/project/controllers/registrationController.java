package com.spring.project.controllers;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.spring.project.users.*;
import com.spring.project.services.UserRepository;

import java.io.UnsupportedEncodingException;

@Controller
public class registrationController {

    @Autowired
    private UserRepository userRepo; // Reference to user repository interface

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/confirm")
    public String confirm() { 
        return "confirmation";
    } // confirm

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    } // signup

    @PostMapping("/process_register")
    public String processRegister(User user) throws MessagingException, UnsupportedEncodingException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // checking if email exists
        // TODO: Have reg page say duplicate email
       User existingAccount = userRepo.findByEmail(user.getEmail());
        if (existingAccount != null)
        {
            return "signup";
        }


        sendVerEmail(user);
        userRepo.save(user);
    
        return "register_success";
    }

    public void sendVerEmail(User user) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("tidalwavetheaters@gmail.com", "test");
        helper.setTo(user.getEmail());
        helper.setSubject("testSub");
        helper.setText("test text");

        mailSender.send(message);

    }

}
