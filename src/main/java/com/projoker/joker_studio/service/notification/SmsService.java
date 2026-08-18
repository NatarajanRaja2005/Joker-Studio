package com.projoker.joker_studio.service.notification;

import com.projoker.joker_studio.repository.SmsVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService{
    private final SmsVerificationRepository smsVerificationRepository;

    public void otpVerification(Long phone, String message) {
        final String format =
                "Joker's Studio: Your OTP for mobile number verification is "
                        + message
                        + ". This OTP is valid for 10 minutes. "
                        + "Please do not share this OTP with anyone.";

    }
}
