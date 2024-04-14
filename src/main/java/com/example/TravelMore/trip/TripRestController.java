package com.example.TravelMore.trip;

import com.example.TravelMore.UserAccount.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripRestController {

    private final TripService tripService;

    @Autowired
    public TripRestController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/add")
    public ResponseEntity<Trip> addTrip(@Valid @RequestBody Trip trip) {
        Trip addedTrip = tripService.addTrip(trip);
        return new ResponseEntity<>(addedTrip, HttpStatus.CREATED);
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<Trip> removeTripById(@PathVariable Long tripId) {
        Trip removedTrip = tripService.removeTripById(tripId);
        return new ResponseEntity<>(removedTrip, HttpStatus.OK);
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<Trip> updateTrip(@PathVariable Long tripId, @Valid @RequestBody Trip updatedTrip) {
        Trip updated = tripService.updateTrip(tripId, updatedTrip);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PostMapping("/{tripId}/participants")
    public ResponseEntity<Trip> addParticipantToTrip(@PathVariable Long tripId, @Valid @RequestBody User participant) {
        Trip updatedTrip = tripService.addParticipantToTrip(tripId, participant);
        return new ResponseEntity<>(updatedTrip, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Trip>> getAllTrips() {
        List<Trip> trips = tripService.getAllTrips();
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripById(@PathVariable Long tripId) {
        Trip trip = tripService.getTripById(tripId);
        return new ResponseEntity<>(trip, HttpStatus.OK);
    }
}
