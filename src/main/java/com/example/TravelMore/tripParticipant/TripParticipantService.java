package com.example.TravelMore.tripParticipant;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserService;
import com.example.TravelMore.trip.Trip;
import com.example.TravelMore.trip.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
public class TripParticipantService {

//    private final TripParticipantRepository tripParticipantRepository;
//    private final UserService userService;
//    private final TripService tripService;
//
//    @Autowired
//    public TripParticipantService(TripParticipantRepository tripParticipantRepository, UserService userService, TripService tripService) {
//        this.tripParticipantRepository = tripParticipantRepository;
//        this.userService = userService;
//        this.tripService = tripService;
//    }
//
//    public TripParticipant addParticipantToTrip(Long tripId, Long userId) {
//        Trip trip = tripService.getTripById(tripId);
//        User participant = userService.getUserById(userId);
//        if (trip == null || participant == null) {
//            throw new IllegalArgumentException("Trip or user not found");
//        }
//
//        if (tripParticipantRepository.existsByTripAndUser(trip, participant)) {
//            throw new IllegalArgumentException("User is already a participant in the trip.");
//        }
//        TripParticipant tripParticipant = new TripParticipant(trip, participant);
//        return tripParticipantRepository.save(tripParticipant);
//    }
//
//    public List<TripParticipant> getParticipantsByTrip(Trip trip) {
//        return tripParticipantRepository.findByTripId(trip.getId());
//    }
//
//    public void removeParticipantFromTrip(Long tripId, Long participantId) {
//        Trip trip = tripService.getTripById(tripId);
//        User participant = userService.getUserById(participantId);
//        if (trip == null || participant == null) {
//            throw new IllegalArgumentException("Trip or user not found");
//        }
//        TripParticipant tripParticipant = tripParticipantRepository.findByTripAndUser(trip, participant);
//        if (tripParticipant != null) {
//            tripParticipantRepository.delete(tripParticipant);
//        } else {
//            throw new IllegalArgumentException("Participant is not associated with the trip.");
//        }
//    }
//
//    public List<TripParticipant> getTripsByParticipant(User user) {
//        return tripParticipantRepository.findTripsByUser(user);
//    }
}
