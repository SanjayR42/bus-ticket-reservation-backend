package com.bus.reservation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;   // ✅ required for .amount(totalAmount)

    private String paymentMethod; // CARD, UPI, NETBANKING, etc.

    private String status;   // SUCCESS / FAILED

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
}
