package com.example.TravelMore.tripParticipant;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.trip.Trip;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripParticipantServiceTest {

    @Mock
    private TripParticipantRepository tripParticipantRepository;

    @InjectMocks
    private TripParticipantService tripParticipantService;

//    @Test
//    void testAddParticipantToTrip() {
//        // Arrange
//        Trip trip = new Trip();
//        trip.setId(1L);
//        User participant = new User();
//
//        when(tripParticipantRepository.existsByTripAndUser(any(Trip.class), any(User.class))).thenReturn(false);
//        when(tripParticipantRepository.save(any(TripParticipant.class))).thenAnswer(invocation -> {
//            TripParticipant argument = invocation.getArgument(0);
//            argument.setId(1L);
//            return argument;
//        });

        // Act
//        TripParticipant addedParticipant = tripParticipantService.addParticipantToTrip(trip, participant);
//
//        // Assert
//        assertNotNull(addedParticipant);
//        assertNotNull(addedParticipant.getTrip());
//        assertNotNull(addedParticipant.getTrip().getId());
//        assertEquals(trip.getId(), addedParticipant.getTrip().getId());
//        assertEquals(participant, addedParticipant.getParticipant());
//        verify(tripParticipantRepository, times(1)).existsByTripAndUser(trip, participant);
//        verify(tripParticipantRepository, times(1)).save(any(TripParticipant.class));
//    }

    @Test
    void testAddParticipantToTrip_ParticipantAlreadyExists() {
        // Arrange
        Trip trip = new Trip();
        User participant = new User();

        when(tripParticipantRepository.existsByTripAndUser(any(Trip.class), any(User.class))).thenReturn(true);

        // Act and Assert
//        assertThrows(IllegalArgumentException.class, () -> tripParticipantService.addParticipantToTrip(trip, participant));
        verify(tripParticipantRepository, times(1)).existsByTripAndUser(trip, participant);
        verify(tripParticipantRepository, never()).save(any(TripParticipant.class));
    }

    @Test
    void testGetParticipantsByTrip() {
        // Arrange
        Trip trip = new Trip();
        List<TripParticipant> participants = new ArrayList<>();
        when(tripParticipantRepository.findByTripId(trip.getId())).thenReturn(participants);

        // Act
        List<TripParticipant> retrievedParticipants = tripParticipantService.getParticipantsByTrip(trip);

        // Assert
        assertNotNull(retrievedParticipants);
        assertEquals(participants, retrievedParticipants);
        verify(tripParticipantRepository, times(1)).findByTripId(trip.getId());
    }

    @Test
    void testRemoveParticipantFromTrip() {
        // Arrange
        Trip trip = new Trip();
        Long participantId = 1L;
        TripParticipant tripParticipant = new TripParticipant(trip, new User());
        when(tripParticipantRepository.findByTripIdAndUserId(trip.getId(), participantId)).thenReturn(tripParticipant);

        // Act
//        tripParticipantService.removeParticipantFromTrip(trip, participantId);

        // Assert
        verify(tripParticipantRepository, times(1)).delete(tripParticipant);
    }

    @Test
    void testRemoveParticipantFromTrip_ParticipantNotAssociated() {
        // Arrange
        Trip trip = new Trip();
        Long participantId = 1L;
        when(tripParticipantRepository.findByTripIdAndUserId(trip.getId(), participantId)).thenReturn(null);

        // Act and Assert
//        assertThrows(IllegalArgumentException.class, () -> tripParticipantService.removeParticipantFromTrip(trip, participantId));
        verify(tripParticipantRepository, times(1)).findByTripIdAndUserId(trip.getId(), participantId);
        verify(tripParticipantRepository, never()).delete(any(TripParticipant.class));
    }
}