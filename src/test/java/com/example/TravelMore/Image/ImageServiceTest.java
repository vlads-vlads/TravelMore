package com.example.TravelMore.Image;

import com.example.TravelMore.trip.Trip;
import com.example.TravelMore.trip.TripService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private TripService tripService;

    @InjectMocks
    private ImageService imageService;

    @Test
    void testUploadPhotos() throws IOException {
        // Arrange
        MultipartFile[] files = { /* mock MultipartFile objects */ };
        Long tripId = 1L;
        Trip trip = new Trip();
        when(tripService.getTripById(tripId)).thenReturn(trip);

        // Act
        imageService.uploadPhotos(files, tripId);

        // Assert
        verify(tripService, times(1)).getTripById(tripId);
        verify(imageRepository, times(files.length)).save(any(Image.class));
    }

    @Test
    void testUploadPhotos_TripNotFound() {
        // Arrange
        MultipartFile[] files = { /* mock MultipartFile objects */ };
        Long tripId = 1L;
        when(tripService.getTripById(tripId)).thenReturn(null);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> imageService.uploadPhotos(files, tripId));
        verify(tripService, times(1)).getTripById(tripId);
        verify(imageRepository, never()).save(any(Image.class));
    }

    @Test
    void testGetImagesByTrip() {
        // Arrange
        Trip trip = new Trip();
        List<Image> expectedImages = new ArrayList<>();
        when(imageRepository.findByTrip(trip)).thenReturn(expectedImages);

        // Act
        List<Image> result = imageService.getImagesByTrip(trip);

        // Assert
        assertEquals(expectedImages, result);
        verify(imageRepository, times(1)).findByTrip(trip);
    }
}