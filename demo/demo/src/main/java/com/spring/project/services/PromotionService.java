package com.spring.project.services;

import com.spring.project.models.Promotion;
import com.spring.project.users.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private JavaMailSender mailSender;
    public void createNewPromotion(Promotion promotion) {
        //add additional logic to validate the promotion details.
        // check if the promo code is unique, if the start date is before the end date, etc.

        promotion.setSent(false); // Ensure the promotion is marked as not sent when created
        promoRepository.save(promotion);
    }

    // Method to send a promotion to all subscribed users
    public void sendPromotionToSubscribedUsers(Long promotionId) throws MessagingException, UnsupportedEncodingException {
        Optional<Promotion> promotionOptional = promoRepository.findById(promotionId);
        if (promotionOptional.isEmpty()) {
            throw new IllegalStateException("Promotion not found");
        }
        Promotion promotion = promotionOptional.get();
        if (promotion.isSent()) {

            throw new IllegalStateException("Promotion has already been sent and cannot be modified.");
        }

        List<User> subscribedUsers = userRepository.findBySubscription(true);
        // sending an email to each user

        Date startDate = promotion.getStartDate();
        Date endDate = promotion.getEndDate();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);

        String date = formattedStartDate + " till " + formattedEndDate;
        String promoCode = promotion.getPromoCode();
        String promoMessage = "Get " + promotion.getPercentage() + "% off on your next booking! Offer valid till " + date;
        for (User user : subscribedUsers) {
            sendPromoEmail(user, promoCode, promoMessage);
        }
        // After successfully sending the promotion, mark it as sent
        promotion.setSent(true);
        promoRepository.save(promotion);
    }
    public void sendPromoEmail(User user, String promoCode, String promoMessage) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("tidalwavetheaters@gmail.com", "Tidal Wave Theaters");
        helper.setTo(user.getEmail());
        helper.setSubject("Exclusive Promotion for You!");

        String content = "<h1>Exclusive Promotion Just for You!</h1>"
                + "<p>Hi " + user.getFirstName() + ",</p>"
                + "<p>We're excited to offer you an exclusive promotion as our valued subscriber.</p>"
                + "<p><b>Promo Code: " + promoCode + "</b></p>"
                + "<p>" + promoMessage + "</p>"
                + "<p>Use this code at checkout to enjoy your discount.</p>"
                + "<p>Thank you for choosing Tidal Wave Theaters!</p>"
                + "<p><i>- The Tidal Wave Theaters Team</i></p>";

        helper.setText(content, true);

        mailSender.send(message);
    }

}
