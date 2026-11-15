package com.inn.cafe.Cafe.Management.rest;

import com.inn.cafe.Cafe.Management.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/sendOtp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        if (email == null || email.trim().isEmpty()) {
            return new ResponseEntity<>("Email is required.", HttpStatus.BAD_REQUEST);
        }

        String resultMessage = otpService.sendOtp(email);

        return new ResponseEntity<>(resultMessage, HttpStatus.OK);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(email, otp);

        if (!isValid) {
            return new ResponseEntity<>("Invalid or expired OTP.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("OTP verified successfully.", HttpStatus.OK);
    }
}