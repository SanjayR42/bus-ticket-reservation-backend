package com.bus.reservation.controller;

import com.bus.reservation.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // ===============================
    // Admin: Get total revenue
    // ===============================
    @GetMapping("/revenue/total")
    public ResponseEntity<Double> getTotalRevenue() {
        return ResponseEntity.ok(reportService.getTotalRevenue());
    }

    // ===============================
    // Admin: Daily booking summary
    // ===============================
    @GetMapping("/bookings/daily")
    public ResponseEntity<Map<?, Long>> getDailyBookings() {
        return ResponseEntity.ok(reportService.getDailyBookingSummary());
    }

    // ===============================
    // Admin: Revenue by route
    // ===============================
    @GetMapping("/revenue/by-route")
    public ResponseEntity<Map<String, Double>> getRevenueByRoute() {
        return ResponseEntity.ok(reportService.getRevenueByRoute());
    }

    // ===============================
    // Admin: Top N routes by revenue
    // ===============================
    @GetMapping("/revenue/top-routes")
    public ResponseEntity<List<Map.Entry<String, Double>>> getTopRoutes(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(reportService.getTopRoutes(limit));
    }
}
