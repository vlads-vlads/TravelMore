package com.example.TravelMore.trip;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final UserService userService;

    @Autowired
    public TripService(TripRepository tripRepository, UserService userService) {
        this.tripRepository = tripRepository;
        this.userService = userService;
    }

    public Trip addTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public Trip removeTripById(Long tripId) {
        Trip removedTrip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip with ID " + tripId + " does not exist"));
        tripRepository.deleteById(tripId);
        return removedTrip;
    }

    public Trip updateTrip(Long tripId, Trip updatedTrip) {
        Trip existingTrip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        existingTrip.setName(updatedTrip.getName());
        existingTrip.setDestination(updatedTrip.getDestination());
        existingTrip.setStartDate(updatedTrip.getStartDate());
        existingTrip.setEndDate(updatedTrip.getEndDate());
        return tripRepository.save(existingTrip);
    }

    public Trip addParticipantToTrip(Long tripId, User participant) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        List<User> participants = trip.getParticipants();
        participants.add(participant);
        trip.setParticipants(participants);
        return tripRepository.save(trip);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Trip getTripById(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip with ID " + tripId + " not found"));
    }

}
