package com.example.TravelMore.tripParticipant;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.trip.Trip;
import com.example.TravelMore.trip.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip-participants")
public class TripParticipantRestController {

    private final TripParticipantService tripParticipantService;
    private final TripService tripService;

    @Autowired
    public TripParticipantRestController(TripParticipantService tripParticipantService, TripService tripService) {
        this.tripParticipantService = tripParticipantService;
        this.tripService = tripService;
    }

    @PostMapping("/{tripId}/add")
    public ResponseEntity<TripParticipant> addParticipantToTrip(@PathVariable Long tripId, @RequestBody User participant) {
        Trip trip = tripService.getTripById(tripId);

        TripParticipant addedParticipant = tripParticipantService.addParticipantToTrip(trip, participant);

        return new ResponseEntity<>(addedParticipant, HttpStatus.CREATED);
    }

    @GetMapping("/{tripId}/participants")
    public ResponseEntity<List<TripParticipant>> getParticipantsByTrip(@PathVariable Long tripId) {
        Trip trip = tripService.getTripById(tripId);
        List<TripParticipant> participants = tripParticipantService.getParticipantsByTrip(trip);
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

}
