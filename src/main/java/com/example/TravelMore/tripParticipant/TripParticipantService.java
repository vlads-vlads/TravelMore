package com.example.TravelMore.tripParticipant;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripParticipantService {

    private final TripParticipantRepository tripParticipantRepository;

    @Autowired
    public TripParticipantService(TripParticipantRepository tripParticipantRepository) {
        this.tripParticipantRepository = tripParticipantRepository;
    }

    public TripParticipant addParticipantToTrip(Trip trip, User participant) {
        if (tripParticipantRepository.existsByTripAndUser(trip, participant)) {
            throw new IllegalArgumentException("User is already a participant in the trip.");
        }

        TripParticipant tripParticipant = new TripParticipant();
        tripParticipant.setTrip(trip);
        tripParticipant.setUser(participant);
        return tripParticipantRepository.save(tripParticipant);
    }

    public List<TripParticipant> getParticipantsByTrip(Trip trip) {
        return tripParticipantRepository.findByTripId(trip.getId());
    }

}
