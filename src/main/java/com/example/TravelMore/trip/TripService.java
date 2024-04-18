package com.example.TravelMore.trip;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserService;
import com.example.TravelMore.tripParticipant.TripParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final UserService userService;
//    private final TripParticipantService tripParticipantService;

    @Autowired
    public TripService(TripRepository tripRepository, UserService userService) {
        this.tripRepository = tripRepository;
        this.userService = userService;
//        this.tripParticipantService = tripParticipantService;
    }

    public Trip createTrip(Trip trip, Long creatorId) {
        User creator = userService.getUserById(creatorId);
        if (creator != null) {
            trip.setCreator(creator);
            return tripRepository.save(trip);
        } else {
            throw new IllegalArgumentException("Creator not found");
        }
    }

    public Trip updateTrip(Trip trip) {
        if (tripRepository.existsById(trip.getId())) {
            return tripRepository.save(trip);
        } else {
            throw new IllegalArgumentException("Trip not found");
        }
    }

    public void deleteTrip(Long tripId) {
        if (tripRepository.existsById(tripId)) {
            tripRepository.deleteById(tripId);
        } else {
            throw new IllegalArgumentException("Trip not found");
        }
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Trip getTripById(Long tripId) {
        return tripRepository.findById(tripId).orElse(null);
    }

    public List<Trip> getTripsByCreatorId(Long creatorId) {
        User creator = userService.getUserById(creatorId);
        if (creator != null) {
            return tripRepository.findByCreator(creator);
        } else {
            throw new IllegalArgumentException("Creator not found");
        }
    }

//    public void addParticipantToTrip(Long tripId, Long userId) {
//        Trip trip = getTripById(tripId);
//        User participant = userService.getUserById(userId);
//
//        if (trip == null || participant == null) {
//            throw new IllegalArgumentException("Trip or user not found");
//        }
//        tripParticipantService.addParticipantToTrip(tripId, userId);
//    }
//
//    public void removeParticipantFromTrip(Long tripId, Long participantId) {
//        Trip trip = getTripById(tripId);
//        User participant = userService.getUserById(participantId);
//
//        if (trip == null || participant == null) {
//            throw new IllegalArgumentException("Trip or user not found");
//        }
//        tripParticipantService.removeParticipantFromTrip(tripId, participantId);
//    }
}
