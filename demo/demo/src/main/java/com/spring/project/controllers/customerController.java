package com.spring.project.controllers;

import com.spring.project.services.CardInfoRepository;
import com.spring.project.services.Encrypt;
import com.spring.project.services.UserRepository;
import com.spring.project.users.User;
import com.spring.project.users.userinfo.CardInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class customerController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CardInfoRepository cardRepo;

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
    public String creditcards(Principal principal, Model model) throws Exception {
        if (principal == null)
        {
            return "homepage";
        }

        // Copying off Sam here O_o
        User edited = userRepo.findByEmail(principal.getName());
        List<CardInfo> cards = edited.getPaymentInfo(); // Gets each list of card info

        for(int i = 0; i < cards.size(); i++) {
            if(!cards.get(i).getCardNumber().equals("")) {
                KeyStore secretKeyTarget = KeyStore.getInstance("JCEKS");
                char[] ksPassword = "password".toCharArray();
                try (FileInputStream fis = new FileInputStream("keystore.jceks")) {
                    secretKeyTarget.load(fis, ksPassword);
                }
                KeyStore.ProtectionParameter protPassword = new KeyStore.PasswordProtection(ksPassword);
                KeyStore.SecretKeyEntry tempKey = (KeyStore.SecretKeyEntry) secretKeyTarget.getEntry(cards.get(i).getId().toString(), protPassword);
                SecretKey retrievedKey = new SecretKeySpec(tempKey.getSecretKey().getEncoded(), "AES");
                Encrypt encryptor = new Encrypt();
                String decCardNum = encryptor.decrypt(cards.get(i).getCardNumber(), retrievedKey);
                cards.get(i).setCardNumber(decCardNum);
            } // if
        } // for

        model.addAttribute("cards", cards);
        return "creditcards";
    } // creditcards

    @GetMapping("/editcards/{id}")
    public String editcards(@PathVariable Long id, Model model) throws Exception {
        CardInfo card = cardRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Card ID"));

        KeyStore secretKeyTarget = KeyStore.getInstance("JCEKS");
        char[] ksPassword = "password".toCharArray();
        try(FileInputStream fis = new FileInputStream("keystore.jceks")) {
            secretKeyTarget.load(fis, ksPassword);
        }
        KeyStore.ProtectionParameter protPassword = new KeyStore.PasswordProtection(ksPassword);
        KeyStore.SecretKeyEntry tempKey = (KeyStore.SecretKeyEntry) secretKeyTarget.getEntry(card.getId().toString(), protPassword);
        SecretKey retrievedKey = new SecretKeySpec(tempKey.getSecretKey().getEncoded(), "AES");
        Encrypt encryptor = new Encrypt();
        String decCardNum = encryptor.decrypt(card.getCardNumber(), retrievedKey);
        card.setCardNumber(decCardNum);

        model.addAttribute("card", card);
        return "editcards";
    } // editcards

    @PostMapping("/editcards")
    public String saveCard(@ModelAttribute CardInfo card, Principal principal) throws Exception {
        User user = userRepo.findByEmail(principal.getName());
        CardInfo editedCard = cardRepo.findById(card.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid Card ID"));
        editedCard.setId(card.getId());
        editedCard.setCardName(card.getCardName());
        editedCard.setExpirationDate(card.getExpirationDate());
        editedCard.setSecurityCode(card.getSecurityCode());
        editedCard.setUserId(user);
        editedCard.setCardNumber(card.getCardNumber());

        File keystoreFile = new File("keystore.jceks");


        // TEST
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        char[] ksPassword = "password".toCharArray();

        if(keystoreFile.exists()) {
            try(FileInputStream fis = new FileInputStream(keystoreFile)) {
                keyStore.load(fis, ksPassword);
            }
        } else {
            keyStore.load(null, ksPassword);
        } // else

        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
        KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(ksPassword);
        keyStore.setEntry(card.getId().toString(), secretKeyEntry, entryPassword);

        try(FileOutputStream fos = new FileOutputStream(keystoreFile)) {
            keyStore.store(fos, ksPassword);
        }

        String toEnc = editedCard.getCardNumber();
        Encrypt encrypt = new Encrypt();
        editedCard.setCardNumber(encrypt.encrypt(toEnc, secretKey));
        // TEST

        cardRepo.save(editedCard);
        return "redirect:/creditcards";
    }

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

