package com.inn.cafe.Cafe.Management.service;

import com.inn.cafe.Cafe.Management.customExceptions.InvalidEmailException;
import com.inn.cafe.Cafe.Management.pojo.OtpData;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final JavaMailSender mailSender;

    // In-memory storage mapping Email -> OtpData
    private final Map<String, OtpData> otpCache = new ConcurrentHashMap<>();

    // Simple standard regex for email validation
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    public String sendOtp(String email) {
        // 1. Check if email is null or doesn't match a valid format
        if (email == null || !Pattern.matches(EMAIL_REGEX, email.trim())) {
            throw new InvalidEmailException("Please provide a valid email address.");
        }

        try {
            // Generate a 6-digit random OTP
            String generatedOtp = String.valueOf(100000 + new Random().nextInt(900000));

            // Set expiration time to exactly 10 minutes from now
            Date expiryTime = new Date(System.currentTimeMillis() + 10 * 60 * 1000);

            // Store in memory cache
            otpCache.put(email, new OtpData(generatedOtp, expiryTime));

            // Send email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Cafe Management System - Email Verification OTP");
            message.setText("Your verification OTP is: " + generatedOtp + "\nIt is valid for 10 minutes.");
            mailSender.send(message);

            return "OTP sent successfully.";
        } catch (Exception e) {
            // Remove from cache if mail sending fails
            otpCache.remove(email);
            // Throw your custom exception handled by @RestControllerAdvice
            throw new InvalidEmailException("Failed to send OTP. Please check the email address.");
        }
    }

    public boolean verifyOtp(String email, String submittedOtp) {
        OtpData storedData = otpCache.get(email);

        if (storedData == null) {
            return false;
        }

        if (storedData.otp().equals(submittedOtp) && new Date().before(storedData.expirationTime())) {
            otpCache.remove(email);
            return true;
        }

        return false;
    }
}