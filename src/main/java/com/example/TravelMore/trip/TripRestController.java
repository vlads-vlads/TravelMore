package com.example.TravelMore.trip;

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

    @GetMapping("/all")
    public ResponseEntity<List<Trip>> getAllTrips() {
        List<Trip> trips = tripService.getAllTrips();
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripById(@PathVariable Long tripId) {
        Trip trip = tripService.getTripById(tripId);
        return ResponseEntity.ok(trip);
    }

    @PostMapping("/add")
    public ResponseEntity<Trip> addTrip(@RequestBody Trip trip) {
        Trip addedTrip = tripService.addTrip(trip);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedTrip);
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<Trip> updateTrip(@PathVariable Long tripId, @RequestBody Trip updatedTrip) {
        Trip trip = tripService.updateTrip(tripId, updatedTrip);
        return ResponseEntity.ok(trip);
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> removeTripById(@PathVariable Long tripId) {
        tripService.removeTripById(tripId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/created-by/{userId}")
    public ResponseEntity<List<Trip>> getTripsCreatedByUser(@PathVariable Long userId) {
        List<Trip> trips = tripService.getTripsCreatedByUser(userId);
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/participated-by/{userId}")
    public ResponseEntity<List<Trip>> getParticipatedTrips(@PathVariable Long userId) {
        List<Trip> trips = tripService.getParticipatedTrips(userId);
        return ResponseEntity.ok(trips);
    }
}
