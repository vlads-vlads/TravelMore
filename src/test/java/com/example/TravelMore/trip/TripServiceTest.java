package com.example.TravelMore.trip;

import com.example.TravelMore.UserAccount.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private TripService tripService;

    @Test
    void testAddTrip() {
        // Arrange
        Trip trip = new Trip();
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);

        // Act
        Trip savedTrip = tripService.addTrip(trip);

        // Assert
        assertNotNull(savedTrip);
        verify(tripRepository, times(1)).save(trip);
    }

    @Test
    void testRemoveTripById() {
        // Arrange
        Long tripId = 1L;
        Trip trip = new Trip();
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));

        // Act
        Trip removedTrip = tripService.removeTripById(tripId);

        // Assert
        assertNotNull(removedTrip);
        verify(tripRepository, times(1)).deleteById(tripId);
    }

    @Test
    void testUpdateTrip() {
        // Arrange
        Long tripId = 1L;
        Trip existingTrip = new Trip();
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(existingTrip));
        when(tripRepository.save(any(Trip.class))).thenReturn(existingTrip);

        // Act
        Trip updatedTrip = tripService.updateTrip(tripId, existingTrip);

        // Assert
        assertNotNull(updatedTrip);
        verify(tripRepository, times(1)).save(existingTrip);
    }

    @Test
    void testAddParticipantToTrip() {
        // Arrange
        Long tripId = 1L;
        Trip trip = new Trip();
        List<User> participants = new ArrayList<>();
        trip.setParticipants(participants);
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);
        User participant = new User();

        // Act
        Trip updatedTrip = tripService.addParticipantToTrip(tripId, participant);

        // Assert
        assertNotNull(updatedTrip);
        assertEquals(1, updatedTrip.getParticipants().size());
    }

    @Test
    void testGetAllTrips() {
        // Arrange
        List<Trip> trips = new ArrayList<>();
        when(tripRepository.findAll()).thenReturn(trips);

        // Act
        List<Trip> allTrips = tripService.getAllTrips();

        // Assert
        assertNotNull(allTrips);
        assertEquals(trips.size(), allTrips.size());
    }

    @Test
    void testGetTripById() {
        // Arrange
        Long tripId = 1L;
        Trip trip = new Trip();
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));

        // Act
        Trip foundTrip = tripService.getTripById(tripId);

        // Assert
        assertNotNull(foundTrip);
        assertEquals(trip, foundTrip);
    }
}
