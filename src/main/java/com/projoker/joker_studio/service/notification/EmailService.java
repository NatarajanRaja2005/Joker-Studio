package com.projoker.joker_studio.service.notification;

import com.projoker.joker_studio.model.NotifyMessage;
import com.projoker.joker_studio.model.User;
import com.projoker.joker_studio.response.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService implements INotificationService{
    private final JavaMailSender mailSender;

    @Override
    public void notify(User user, NotifyMessage message) {
        try {
            MimeMessage mailMessage=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mailMessage,false);
            helper.setFrom("natarajanraja10140@gmail.com","Joker's Studio");
            helper.setTo(user.getEmail());

            helper.setSubject(
                    "Dear "+user.getFirstName() +", "+message.getSubject()
            );
            final String formatMessage=message.getMessage()+"\n\n\n Thank you for choosing JokerStudio " +
                    "for the service you needed."+"\n\nFor More info: joker@gmail.com";
            helper.setText(formatMessage);

            mailSender.send(mailMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void optVerification(String email, String message) {
        try {
            MimeMessage mimeMessage=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,false);
            helper.setTo(email);
            helper.setFrom("natarajanraja10140@gmail.com","Joker's Studio");

            helper.setSubject(
                    "Otp verification: Joker's Studio"
            );
            final String formatMessage="Verification for creating your account: \n"+message+" is an OTP for your email. Valid only with in 10 minutes.\n\n\n Thank you for choosing JokerStudio " +
                    "for the service you needed."+"\n\nFor More info: joker@gmail.com";
            helper.setText(formatMessage);
            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to send email", e);
        }

    }
}
