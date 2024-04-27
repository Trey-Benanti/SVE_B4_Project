package com.spring.project.controllers;

import com.spring.project.models.users.User;
import com.spring.project.models.users.userinfo.CardInfo;
import com.spring.project.models.users.userservices.UserRepository;
import com.spring.project.services.Encrypt;

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

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
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
    @RequestParam("billingAddrStreet1") String billingAddrStreet1,
    @RequestParam("billingAddrState1") String billingAddrState1,
    @RequestParam("billingAddrZip1") String billingAddrZip1,
    @RequestParam("billingAddrStreet2") String billingAddrStreet2,
    @RequestParam("billingAddrState2") String billingAddrState2,
    @RequestParam("billingAddrZip2") String billingAddrZip2,
    @RequestParam("billingAddrStreet3") String billingAddrStreet3,
    @RequestParam("billingAddrState3") String billingAddrState3,
    @RequestParam("billingAddrZip3") String billingAddrZip3,
        User user, Model model) throws Exception {
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


        // Adds credit card info to paymentInfo list.
        CardInfo card1 = new CardInfo(user, cn1, cna1, ex1, cv1, billingAddrStreet1, billingAddrState1, billingAddrZip1);
        CardInfo card2 = new CardInfo(user, cn2, cna2, ex2, cv2, billingAddrStreet2, billingAddrState2, billingAddrZip2);
        CardInfo card3 = new CardInfo(user, cn3, cna3, ex3, cv3, billingAddrStreet3, billingAddrState3, billingAddrZip3);
        user.getPaymentInfo().add(card1);
        user.getPaymentInfo().add(card2);
        user.getPaymentInfo().add(card3);

        generateCode(user);
        sendVerEmail(user);

        userRepo.save(user); // Save card info to get IDs.

        File keystoreFile = new File("keystore.jceks");

        for(int i = 0; i < user.getPaymentInfo().size(); i++) { // encrypt card number if exists
            if(!user.getPaymentInfo().get(i).getCardNumber().equals("")) {

                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES"); // Generate secret key
                keyGenerator.init(128);
                SecretKey secretKey = keyGenerator.generateKey();
                KeyStore keyStore = KeyStore.getInstance("JCEKS");
                char[] ksPassword = "password".toCharArray();

                if(keystoreFile.exists()) { // Create key store if none exists
                    try(FileInputStream fis = new FileInputStream(keystoreFile)) {
                        keyStore.load(fis, ksPassword);
                    }
                } else {
                    keyStore.load(null, ksPassword);
                } // else

                KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey); // Set keystore entry params
                KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(ksPassword);
                keyStore.setEntry(user.getPaymentInfo().get(i).getId().toString(), secretKeyEntry, entryPassword);

                try(FileOutputStream fos = new FileOutputStream(keystoreFile)) {
                    keyStore.store(fos, ksPassword);
                } // try

                Encrypt encrypt = new Encrypt(); // Encrypt card number
                user.getPaymentInfo().get(i).setCardNumber(encrypt.encrypt(user.getPaymentInfo().get(i).getCardNumber(), secretKey));
            } // if
        } // for

        userRepo.save(user); // Update the repo with encrypted card number. Requires IDs from first save.
    
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
