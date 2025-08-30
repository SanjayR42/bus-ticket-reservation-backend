package com.bus.reservation.controller;

import com.bus.reservation.model.Booking;
import com.bus.reservation.model.User;
import com.bus.reservation.repository.UserRepository;
import com.bus.reservation.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    // ===============================
    // Customer: Book seats
    // ===============================
    @PostMapping
    public ResponseEntity<Booking> createBooking(
            Authentication authentication,
            @RequestBody Map<String, Object> request) {

        Long tripId = Long.valueOf(request.get("tripId").toString());
        List<Integer> seatIdsRaw = (List<Integer>) request.get("seatIds");
        List<Long> seatIds = seatIdsRaw.stream().map(Long::valueOf).toList();
        String paymentMethod = request.get("paymentMethod").toString();

        // 🔑 Get logged-in user from Authentication
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
                bookingService.createBooking(tripId, seatIds, user.getId(), paymentMethod)
        );
    }

    // ===============================
    // Customer: View my bookings
    // ===============================
    @GetMapping("/me")
    public ResponseEntity<List<Booking>> getMyBookings(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(bookingService.getUserBookings(user.getId()));
    }
}
