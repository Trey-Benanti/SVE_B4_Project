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
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.Random;

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
    public String processRegister(@RequestParam("cn1") String cn1,
    @RequestParam("cna1") String cna1,
    @RequestParam("ex1") String ex1,
    @RequestParam("cv1") String cv1,
    @RequestParam("cn2") String cn2,
    @RequestParam("cna2") String cna2,
    @RequestParam("ex2") String ex2,
    @RequestParam("cv2") String cv2,
    @RequestParam("cn3") String cn3,
    @RequestParam("cna3") String cna3,
    @RequestParam("ex3") String ex3,
    @RequestParam("cv3") String cv3,
        User user, Model model) throws MessagingException, UnsupportedEncodingException {
        // checking if email exists
        User existingAccount = userRepo.findByEmail(user.getEmail());
        if (existingAccount != null)
        {
            model.addAttribute("wrong_email", "Error: Email already in use");
            return "signup";
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.getPaymentInfo().get(0).setCardNumber(cn1);
        user.getPaymentInfo().get(0).setCardName(cna1);
        user.getPaymentInfo().get(0).setExpirationDate(ex1);
        user.getPaymentInfo().get(0).setSecurityCode(cv1);
        user.getPaymentInfo().get(1).setCardNumber(cn2);
        user.getPaymentInfo().get(1).setCardName(cna2);
        user.getPaymentInfo().get(1).setExpirationDate(ex2);
        user.getPaymentInfo().get(1).setSecurityCode(cv2);
        user.getPaymentInfo().get(2).setCardNumber(cn3);
        user.getPaymentInfo().get(2).setCardName(cna3);
        user.getPaymentInfo().get(2).setExpirationDate(ex3);
        user.getPaymentInfo().get(2).setSecurityCode(cv3);

        generateCode(user);
        sendVerEmail(user);

        userRepo.save(user);
    
        return "verification";
    }

    @GetMapping("verification")
    public String verification() {
        return "verification";
    }

    @PostMapping("/verifyUser")
    public String verifyUser(@RequestParam("code") String code, Model model) {
        String ver = "VERIFIED";

        if (code == ver) {
            return verification();
        }

        User user = userRepo.findByVerCode(code);

        if (user == null) {
            model.addAttribute("wrong_code", "Error: Code not found");
            return "verification";
        }

        user.setVerCode(ver);
        userRepo.save(user);

        return "verify_success";
    }

    public void generateCode(User user) {
        Random rand = new Random();

        int code;

        do {
            code = rand.nextInt(900000) + 100000;
        } while (userRepo.findByVerCode(String.valueOf(code)) != null);

        user.setVerCode(String.valueOf(code));
    }

    public void sendVerEmail(User user) throws MessagingException, UnsupportedEncodingException {
        // TODO: make email look nicer and use actual html

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("tidalwavetheaters@gmail.com", "Tidal Wave Theaters");
        helper.setTo(user.getEmail());
        helper.setSubject("Verify Your Account");
        helper.setText(user.getVerCode() + "\nlocalhost:8080/verification");

        mailSender.send(message);

    }

}
