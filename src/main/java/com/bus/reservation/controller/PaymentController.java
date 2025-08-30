package com.bus.reservation.controller;

import com.bus.reservation.model.Payment;
import com.bus.reservation.model.User;
import com.bus.reservation.repository.PaymentRepository;
import com.bus.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    // ===============================
    // Customer: View my payments
    // ===============================
    @GetMapping("/me")
    public ResponseEntity<List<Payment>> getMyPayments(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // fetch payments where booking belongs to this user
        List<Payment> payments = paymentRepository.findByBookingUserId(user.getId());
        return ResponseEntity.ok(payments);
    }

    // ===============================
    // Admin: View all payments
    // ===============================
    @GetMapping("/all")
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentRepository.findAll());
    }
}
