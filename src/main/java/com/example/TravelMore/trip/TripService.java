package com.example.TravelMore.trip;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserService;
import com.example.TravelMore.joinRequest.JoinRequest;
import com.example.TravelMore.joinRequest.JoinRequestRepository;
import com.example.TravelMore.joinRequest.JoinRequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final UserService userService;

    @Autowired
    public TripService(TripRepository tripRepository, JoinRequestRepository joinRequestRepository, UserService userService) {
        this.tripRepository = tripRepository;
        this.joinRequestRepository = joinRequestRepository;
        this.userService = userService;
    }

    public Trip addTrip(Trip trip) {
        trip.setParticipants(Set.of(trip.getCreator())); //TODO check necessity
        return tripRepository.save(trip);
    }

    public void removeTripById(Long tripId) {
        if (tripRepository.existsById(tripId)) {
            tripRepository.deleteById(tripId);
        } else {
            throw new IllegalArgumentException("Trip not found");
        }
    }

    public Trip updateTrip(Trip trip) {
        if (tripRepository.existsById(trip.getId())) {
            return tripRepository.save(trip);
        } else {
            throw new IllegalArgumentException("Trip not found");
        }
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Trip getTripById(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip with ID " + tripId + " not found"));
    }

    public void acceptJoinRequest(Long requestId) {
        JoinRequest joinRequest = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Join request not found"));
        joinRequest.setStatus(JoinRequestStatus.ACCEPTED);

        Trip trip = joinRequest.getTrip();
        User participant = joinRequest.getRequester();
        trip.getParticipants().add(participant);

        joinRequestRepository.save(joinRequest);
        tripRepository.save(trip);
    }

    public void declineJoinRequest(Long requestId) {
        JoinRequest joinRequest = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Join request not found"));
        joinRequest.setStatus(JoinRequestStatus.DECLINED);
        joinRequestRepository.save(joinRequest);
    }

    public List<Trip> getParticipatedTrips(Long userId) {
        return tripRepository.findByParticipantsId(userId);
    }

    public List<Trip> getTripsCreatedByUser(Long userId) {
        return tripRepository.findByCreatorId(userId);
    }

    public Set<User> getTripParticipants(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        return trip.getParticipants();
    }

}
