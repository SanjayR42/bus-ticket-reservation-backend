package com.bus.reservation.repository;

import com.bus.reservation.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByRouteSourceAndRouteDestinationAndDepartureTimeBetween(
            String source, String destination, LocalDateTime start, LocalDateTime end
    );
}
