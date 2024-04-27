package com.spring.project.controllers;

import com.spring.project.models.users.User;
import com.spring.project.models.users.userservices.UserRepository;

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
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Random;

@Controller
public class loginController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JavaMailSender mailSender;
    @GetMapping("/login")
    public String login(Principal principal) {
        if (principal != null)
        {
            return "homepage";
        }

        return "login";
    } // signin

    @GetMapping("/forgotpassword")
    public String forgotpassword() {
        return "forgotpassword";
    }

    @GetMapping("/forgotpassword_success")
    public String forgotpassword_success() {
        return "forgotpassword_success";
    }

    @PostMapping("/forgotpw")
    public String forgotpw(@RequestParam("email") String email,
                           Model model) throws MessagingException, UnsupportedEncodingException {

        User user = userRepo.findByEmail(email);
        if (user == null)
        {
            model.addAttribute("wrong_email", "Email not found");
            return "forgotpassword";
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String tempPw = tempPassword();

        helper.setFrom("tidalwavetheaters@gmail.com", "Tidal Wave Theaters");
        helper.setTo(user.getEmail());
        helper.setSubject("Temp Password");
        helper.setText("Your new temporary password is " + tempPw);

        user.setPassword(passwordEncoder.encode(tempPw));
        userRepo.save(user);

        mailSender.send(message);

        return "forgotpassword_success";
    }

    public String tempPassword() {
        Random rand = new Random();
        int code = rand.nextInt(90000000) + 10000000;
        return String.valueOf(code);
    }

}
