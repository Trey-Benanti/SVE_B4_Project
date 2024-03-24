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
        // TODO: route back to registration page
 /*       User existingAccount = userRepo.findByEmail(user.getEmail());
        if (existingAccount != null)
        {
            return "account_exists";
        }

        // TODO: send verification email and save code (spring-starter-mail)
       // sending verification code
        EmailDetails ver = new EmailDetails();
        ver.setRecipient(user.getEmail());
        ver.setSubject("test");
        ver.setMsgBody("test");

        EmailService sender = new EmailService();
        sender.sendEmail(ver);
*/
        sendVerEmail(user);

        userRepo.save(user);
    
        return "register_success";
    }

    public void sendVerEmail(User user) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("samuelwilson1123@gmail.com", "test");
        helper.setTo(user.getEmail());
        helper.setSubject("testSub");
        helper.setText("test text");

        System.out.println("SENDING to " + user.getEmail());
        mailSender.send(message);

    }

}
