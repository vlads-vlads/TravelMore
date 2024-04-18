package com.example.TravelMore.Image;

import com.example.TravelMore.trip.Trip;
import com.example.TravelMore.trip.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final TripService tripService;

    @Autowired
    public ImageService(ImageRepository imageRepository, TripService tripService) {
        this.imageRepository = imageRepository;
        this.tripService = tripService;
    }

    public void uploadImageToTrip(MultipartFile file, Long tripId) {
        Trip trip = tripService.getTripById(tripId);
        if (trip == null) {
            throw new IllegalArgumentException("Trip not found");
        }

        try {
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            Image image = new Image(base64Image, trip);
            imageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    public List<Image> getImagesByTripId(Long tripId) {
        Trip trip = tripService.getTripById(tripId);
        if (trip == null) {
            throw new IllegalArgumentException("Trip not found");
        }
        return imageRepository.findByTrip(trip);
    }
}
