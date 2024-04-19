package com.example.TravelMore.Image;

import com.example.TravelMore.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    static void deleteByTrip(Trip trip) {
    }
    List<Image> findByTrip(Trip trip);
}
