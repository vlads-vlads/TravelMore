package com.example.TravelMore.trip;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserService;
import com.example.TravelMore.joinRequest.JoinRequest;
import com.example.TravelMore.joinRequest.JoinRequestRepository;
import com.example.TravelMore.joinRequest.JoinRequestStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private JoinRequestRepository joinRequestRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TripService tripService;

    @Test
    void addTrip_EndDateBeforeStartDate() {
        // Arrange
        Trip trip = new Trip();
        trip.setStartDate(new Date(System.currentTimeMillis()));
        trip.setEndDate(new Date(System.currentTimeMillis() - 1000)); // End date before start date

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> tripService.addTrip(trip));
    }

    @Test
    void addTrip_EndDateAfterStartDate_ReturnsSavedTrip() {
        // Arrange
        Trip trip = new Trip();
        trip.setStartDate(new Date(System.currentTimeMillis()));
        trip.setEndDate(new Date(System.currentTimeMillis() + 1000));
        when(tripRepository.save(trip)).thenReturn(trip);

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

        // Act
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
        Trip result = tripService.removeTripById(tripId);

        // Assert
        assertNotNull(result);
        assertEquals(trip, result);
        verify(tripRepository, times(1)).deleteById(tripId);
    }

    @Test
    void testRemoveTripById_TripNotFound() {
        // Arrange
        Long tripId = 1L;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> tripService.removeTripById(tripId));
        verify(tripRepository, never()).deleteById(tripId);
    }

    @Test
    void testUpdateTrip() {
        // Arrange
        Long tripId = 1L;
        Trip existingTrip = new Trip();
        Trip updatedTrip = new Trip();

        // Act
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(existingTrip));
        when(tripRepository.save(existingTrip)).thenReturn(updatedTrip);
        Trip result = tripService.updateTrip(tripId, updatedTrip);

        // Assert
        assertNotNull(result);
        assertEquals(updatedTrip, result);
        verify(tripRepository, times(1)).save(existingTrip);
    }

    @Test
    void testGetAllTrips() {
        // Arrange
        List<Trip> trips = Arrays.asList(new Trip(), new Trip());

        // Act
        when(tripRepository.findAll()).thenReturn(trips);
        List<Trip> result = tripService.getAllTrips();

        // Assert
        assertNotNull(result);
        assertEquals(trips.size(), result.size());
        assertTrue(result.containsAll(trips));
    }

    @Test
    void testGetTripById() {
        // Arrange
        Long tripId = 1L;
        Trip trip = new Trip();

        // Act
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
        Trip result = tripService.getTripById(tripId);

        // Assert
        assertNotNull(result);
        assertEquals(trip, result);
    }

    @Test
    void testGetTripById_TripNotFound() {
        // Arrange
        Long tripId = 1L;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> tripService.getTripById(tripId));
    }

    @Test
    void testAcceptJoinRequest() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Long requestId = 1L;
        JoinRequest joinRequest = new JoinRequest();
        Trip trip = new Trip();
        User participant = new User();
        joinRequest.setTrip(trip);
        joinRequest.setRequester(participant);
        joinRequest.setId(requestId);

        // Act
        when(joinRequestRepository.findById(requestId)).thenReturn(Optional.of(joinRequest));
        tripService.acceptJoinRequest(requestId);

        // Assert
        assertEquals(JoinRequestStatus.ACCEPTED, joinRequest.getStatus());
        assertTrue(trip.getParticipants().contains(participant));
    }

    @Test
    void testDeclineJoinRequest() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Long requestId = 1L;
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setStatus(JoinRequestStatus.PENDING);
        joinRequest.setId(requestId);

        // Act
        when(joinRequestRepository.findById(requestId)).thenReturn(Optional.of(joinRequest));
        tripService.declineJoinRequest(requestId);

        // Assert
        assertEquals(JoinRequestStatus.DECLINED, joinRequest.getStatus());
    }

    @Test
    void testGetParticipatedTrips() {
        // Arrange
        Long userId = 1L;
        List<Trip> trips = Arrays.asList(new Trip(), new Trip());

        // Act
        when(tripRepository.findByParticipantsId(userId)).thenReturn(trips);
        List<Trip> result = tripService.getParticipatedTrips(userId);

        // Assert
        assertNotNull(result);
        assertEquals(trips.size(), result.size());
        assertTrue(result.containsAll(trips));
        verify(tripRepository, times(1)).findByParticipantsId(userId);
    }

    @Test
    void testGetTripsCreatedByUser() {
        // Arrange
        Long userId = 1L;
        List<Trip> trips = Arrays.asList(new Trip(), new Trip());

        // Act
        when(tripRepository.findByCreatorId(userId)).thenReturn(trips);
        List<Trip> result = tripService.getTripsCreatedByUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(trips.size(), result.size());
        assertTrue(result.containsAll(trips));
        verify(tripRepository, times(1)).findByCreatorId(userId);
    }

    @Test
    void testGetTripParticipants() {
        // Arrange
        Long tripId = 1L;
        Trip trip = new Trip();
        Set<User> participants = new HashSet<>();
        participants.add(new User());
        trip.setParticipants(participants);

        // Act
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
        Set<User> result = tripService.getTripParticipants(tripId);

        // Assert
        assertNotNull(result);
        assertEquals(participants.size(), result.size());
        assertTrue(result.containsAll(participants));
        verify(tripRepository, times(1)).findById(tripId);
    }

    @Test
    void testIsUserParticipantInTrip_UserIsNotParticipant() {
        // Arrange
        Long userId = 1L;
        Long tripId = 1L;
        Trip trip = new Trip();
        trip.setId(tripId);
        trip.setParticipants(new HashSet<>());        // Mock trip without participants

        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));

        // Act
        boolean result = tripService.isUserParticipantInTrip(userId, tripId);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsUserParticipantInTrip_TripNotFound() {
        // Arrange
        Long userId = 1L;
        Long tripId = 1L;

        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        // Act
        boolean result = tripService.isUserParticipantInTrip(userId, tripId);

        // Assert
        assertFalse(result);
    }
}