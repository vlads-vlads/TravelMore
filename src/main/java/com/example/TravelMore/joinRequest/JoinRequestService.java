package com.example.TravelMore.joinRequest;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserRepository;
import com.example.TravelMore.trip.Trip;
import com.example.TravelMore.trip.TripRepository;
import com.example.TravelMore.trip.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JoinRequestService {

    private final JoinRequestRepository joinRequestRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    private final TripService tripService;


    @Autowired
    public JoinRequestService(JoinRequestRepository joinRequestRepository, TripRepository tripRepository, UserRepository userRepository, TripService tripService) {
        this.joinRequestRepository = joinRequestRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.tripService = tripService;
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
    public List<JoinRequest> getJoinRequestsForUserRequester(Long userId) {
        return joinRequestRepository.findByRequesterId(userId);
    }



    @Transactional
    public void sendJoinRequestToUser(Long tripId, Long receiver, Long requester) {

        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new RuntimeException("Trip not found"));
        User requestTo = userRepository.findById(receiver).orElseThrow(() -> new RuntimeException("Trip creator not found"));
        User requestFrom = userRepository.findById(requester).orElseThrow(() -> new RuntimeException("Receiver user not found"));

        // Create and save the join request
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setTrip(trip);
        joinRequest.setRequester(requestFrom);
        joinRequest.setReceiver(requestTo);
        joinRequest.setStatus(JoinRequestStatus.PENDING);
        joinRequestRepository.save(joinRequest);
    }

    public List<JoinRequest> getJoinRequestsForTrip(Long tripId) {
        return joinRequestRepository.findByTripId(tripId);
    }

    public List<JoinRequest> getAllJoinRequests() {
       return joinRequestRepository.findAll();
    }

    @Transactional
    public void approveJoinRequest(Long requestId) {
        JoinRequest joinRequest = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Join request not found"));
        tripService.acceptJoinRequest(requestId);

    }


    @Transactional
    public void declineJoinRequest(Long requestId) {
        JoinRequest joinRequest = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Join request not found"));

        joinRequestRepository.delete(joinRequest);
    }


    public boolean existsJoinRequest(Long tripId, Long receiverId, Long requesterId) {
        JoinRequest joinRequest = joinRequestRepository.findByTripIdAndReceiverIdAndRequesterId(tripId, receiverId, requesterId);
        return joinRequest != null;
    }
}
