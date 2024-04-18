package com.example.TravelMore.Image;

import com.example.TravelMore.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByTrip(Trip trip);
}
