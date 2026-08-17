package com.projoker.joker_studio.service.notification;

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

@Service
@RequiredArgsConstructor
public class EmailService implements INotificationService{
    private final JavaMailSender mailSender;

    @Override
    public void notify(User user, String message) {
        try {
            MimeMessage mailMessage=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mailMessage,false);
            helper.setFrom("natarajanraja10140@gmail.com","Joker's Studio");
            helper.setTo(user.getEmail());

            helper.setSubject(
                    user.getFirstName() +
                            ": Status of Your Booking was Changed."
            );
            final String formatMessage=message+"\n\n\n Thank you for choosing JokerStudio " +
                    "for the service you needed."+"\n\nFor More info: joker@gmail.com";
            helper.setText(formatMessage);

            mailSender.send(mailMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
