package com.example.TravelMore.joinRequest;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserRepository;
import com.example.TravelMore.trip.Trip;
import com.example.TravelMore.trip.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JoinRequestService {

    private final JoinRequestRepository joinRequestRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;


    @Autowired
    public JoinRequestService(JoinRequestRepository joinRequestRepository, TripRepository tripRepository, UserRepository userRepository) {
        this.joinRequestRepository = joinRequestRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void sendJoinRequest(Long tripId, Long userId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new RuntimeException("Trip not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setTrip(trip);
        joinRequest.setRequester(user);
        joinRequest.setStatus(JoinRequestStatus.PENDING);

        joinRequestRepository.save(joinRequest);
    }

    @Transactional
    public void processJoinRequest(Long requestId, JoinRequestStatus status) {
        JoinRequest joinRequest = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Join request not found"));

        joinRequest.setStatus(status);
        joinRequestRepository.save(joinRequest);
    }

    @Transactional(readOnly = true)
    public List<JoinRequest> getJoinRequestsForUser(Long userId) {
        return joinRequestRepository.findByRequesterId(userId);
    }

    @Transactional
    public void sendJoinRequestToUser(Long tripId, Long tripCreatorId, Long receiverUserId) {
        // Fetch trip, trip creator, and receiver user from database
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new RuntimeException("Trip not found"));
        User tripCreator = userRepository.findById(tripCreatorId).orElseThrow(() -> new RuntimeException("Trip creator not found"));
        User receiverUser = userRepository.findById(receiverUserId).orElseThrow(() -> new RuntimeException("Receiver user not found"));

        // Create and save the join request
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setTrip(trip);
        joinRequest.setRequester(tripCreator); // Trip creator is the requester
        joinRequest.setReceiver(receiverUser);
        joinRequest.setStatus(JoinRequestStatus.PENDING);
        joinRequestRepository.save(joinRequest);
    }

    public List<JoinRequest> getJoinRequestsForTrip(Long tripId) {
        return joinRequestRepository.findByTripId(tripId);
    }

}
