package com.example.TravelMore.trip;

import com.example.TravelMore.comment.Comment;
import com.example.TravelMore.comment.CommentService;
import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.tripParticipant.TripParticipant;
import com.example.TravelMore.tripParticipant.TripParticipantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips")
public class TripRestController {

    private final TripService tripService;

    @Autowired
    public TripRestController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/create")
    public ResponseEntity<Trip> createTrip(@RequestBody Trip trip, @RequestParam Long creatorId) {
        try {
            Trip createdTrip = tripService.createTrip(trip, creatorId);
            return new ResponseEntity<>(createdTrip, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Trip> updateTrip(@RequestBody Trip trip) {
        try {
            Trip updatedTrip = tripService.updateTrip(trip);
            return new ResponseEntity<>(updatedTrip, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{tripId}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long tripId) {
        try {
            tripService.deleteTrip(tripId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Trip>> getAllTrips() {
        List<Trip> trips = tripService.getAllTrips();
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripById(@PathVariable Long tripId) {
        Trip trip = tripService.getTripById(tripId);
        if (trip != null) {
            return new ResponseEntity<>(trip, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<Trip>> getTripsByCreatorId(@PathVariable Long creatorId) {
        try {
            List<Trip> trips = tripService.getTripsByCreatorId(creatorId);
            return new ResponseEntity<>(trips, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @PostMapping("/{tripId}/addParticipant")
//    public ResponseEntity<Void> addParticipantToTrip(@PathVariable Long tripId, @RequestParam Long userId) {
//        try {
//            tripService.addParticipantToTrip(tripId, userId);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @DeleteMapping("/{tripId}/removeParticipant")
//    public ResponseEntity<Void> removeParticipantFromTrip(@PathVariable Long tripId, @RequestParam Long participantId) {
//        try {
//            tripService.removeParticipantFromTrip(tripId, participantId);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}
