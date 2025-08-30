package com.bus.reservation.controller;

import com.bus.reservation.model.Seat;
import com.bus.reservation.model.Trip;
import com.bus.reservation.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    // Admin: schedule trip
    @PostMapping
    public ResponseEntity<Trip> scheduleTrip(
            @RequestParam Long busId,
            @RequestParam Long routeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalTime,
            @RequestParam Double fare) {
        return ResponseEntity.ok(
                tripService.scheduleTrip(busId, routeId, departureTime, arrivalTime, fare)
        );
    }

    // Search trips by source, destination, date
    @GetMapping("/search")
    public ResponseEntity<List<Trip>> searchTrips(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(tripService.searchTrips(source, destination, date));
    }

    // Get seat availability for trip
    @GetMapping("/{tripId}/seats")
    public ResponseEntity<List<Seat>> getTripSeats(@PathVariable Long tripId) {
        return ResponseEntity.ok(tripService.getTripSeats(tripId));
    }
}
