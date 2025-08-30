package com.bus.reservation.service;

import com.bus.reservation.model.*;
import com.bus.reservation.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;
    private final SeatRepository seatRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Transactional
    public Booking createBooking(Long tripId, List<Long> seatIds, Long userId, String paymentMethod) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        double totalAmount = 0.0;

        // Verify & lock seats
        for (Long seatId : seatIds) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            if (seat.isBooked()) {
                throw new RuntimeException("Seat already booked: " + seat.getSeatNumber());
            }
            seat.setBooked(true);
            seatRepository.save(seat);
            totalAmount += trip.getFare();
        }

        Booking booking = Booking.builder()
                .user(user)
                .trip(trip)
                .seats(seatRepository.findAllById(seatIds))
                .status("CONFIRMED")
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        // Create payment entry
        Payment payment = Payment.builder()
                .booking(savedBooking)
                .amount(totalAmount)
                .paymentMethod(paymentMethod)
                .status("SUCCESS")
                .build();

        paymentRepository.save(payment);

        return savedBooking;
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
}
