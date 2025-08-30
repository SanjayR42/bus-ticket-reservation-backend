package com.bus.reservation.repository;

import com.bus.reservation.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBookingUserId(Long userId); // ✅ custom query
}
