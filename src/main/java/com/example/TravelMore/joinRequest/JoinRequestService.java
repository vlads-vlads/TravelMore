package com.example.TravelMore.joinRequest;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserService;
import com.example.TravelMore.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JoinRequestService {

    private final JoinRequestRepository joinRequestRepository;
    private final UserService userService;

    @Autowired
    public JoinRequestService(JoinRequestRepository joinRequestRepository, UserService userService) {
        this.joinRequestRepository = joinRequestRepository;
        this.userService = userService;
    }

    public JoinRequest createJoinRequest(JoinRequest joinRequest) {
        joinRequest.setStatus(RequestStatus.PENDING);
        return joinRequestRepository.save(joinRequest);
    }

    public List<JoinRequest> getJoinRequestsByTrip(Trip trip) {
        return joinRequestRepository.findByTrip(trip);
    }

    public JoinRequest getJoinRequestById(Long joinRequestId) {
        return joinRequestRepository.findById(joinRequestId).orElse(null);
    }

    public void acceptJoinRequest(Long joinRequestId) {
        JoinRequest joinRequest = joinRequestRepository.findById(joinRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Join request not found with ID: " + joinRequestId));
        joinRequest.setStatus(RequestStatus.ACCEPTED);
        joinRequestRepository.save(joinRequest);
    }

    public void declineJoinRequest(Long joinRequestId) {
        JoinRequest joinRequest = joinRequestRepository.findById(joinRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Join request not found with ID: " + joinRequestId));
        joinRequest.setStatus(RequestStatus.DECLINED);
        joinRequestRepository.save(joinRequest);
    }

//    public List<JoinRequest> getJoinRequestsByUser(User user) {
//        return joinRequestRepository.findByUser(user);
//    }

    public List<JoinRequest> getJoinRequestsByUserId(Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return joinRequestRepository.findByUser(user);
    }
    public List<JoinRequest> getJoinRequestsByTripId(Long tripId) {
        return joinRequestRepository.findByTripId(tripId);
    }
}