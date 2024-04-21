package com.example.TravelMore.joinRequest;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserRepository;
import com.example.TravelMore.trip.Trip;
import com.example.TravelMore.trip.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JoinRequestServiceTest {

    @Mock
    private JoinRequestRepository joinRequestRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JoinRequestService joinRequestService;

    @Test
    void testSendJoinRequest() {
        // Arrange
        Long tripId = 1L;
        Long userId = 1L;
        Trip trip = new Trip();
        User user = new User();
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        joinRequestService.sendJoinRequest(tripId, userId);

        // Assert
        verify(joinRequestRepository, times(1)).save(any(JoinRequest.class));
    }

    @Test
    void testProcessJoinRequest() {
        // Arrange
        Long requestId = 1L;
        JoinRequest joinRequest = new JoinRequest();
        when(joinRequestRepository.findById(requestId)).thenReturn(Optional.of(joinRequest));

        // Act
        joinRequestService.processJoinRequest(requestId, JoinRequestStatus.ACCEPTED);

        // Assert
        assertEquals(JoinRequestStatus.ACCEPTED, joinRequest.getStatus());
        verify(joinRequestRepository, times(1)).save(joinRequest);
    }

    @Test
    void testGetJoinRequestsForUser() {
        // Arrange
        Long userId = 1L;
        List<JoinRequest> expectedJoinRequests = new ArrayList<>();
        when(joinRequestRepository.findByRequesterId(userId)).thenReturn(expectedJoinRequests);

        // Act
        List<JoinRequest> result = joinRequestService.getJoinRequestsForUserRequester(userId);

        // Assert
        assertEquals(expectedJoinRequests, result);
        verify(joinRequestRepository, times(1)).findByRequesterId(userId);
    }

    @Test
    void testSendJoinRequestToUser() {
        // Arrange
        Long tripId = 1L;
        Long tripCreatorId = 2L;
        Long receiverUserId = 3L;
        Trip trip = new Trip();
        User tripCreator = new User();
        User receiverUser = new User();
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));
        when(userRepository.findById(tripCreatorId)).thenReturn(Optional.of(tripCreator));
        when(userRepository.findById(receiverUserId)).thenReturn(Optional.of(receiverUser));

        // Act
        joinRequestService.sendJoinRequestToUser(tripId, tripCreatorId, receiverUserId);

        // Assert
        verify(joinRequestRepository, times(1)).save(any(JoinRequest.class));
    }

    @Test
    void testGetJoinRequestsForTrip() {
        // Arrange
        Long tripId = 1L;
        List<JoinRequest> expectedJoinRequests = new ArrayList<>();
        when(joinRequestRepository.findByTripId(tripId)).thenReturn(expectedJoinRequests);

        // Act
        List<JoinRequest> result = joinRequestService.getJoinRequestsForTrip(tripId);

        // Assert
        assertEquals(expectedJoinRequests, result);
        verify(joinRequestRepository, times(1)).findByTripId(tripId);
    }
}
