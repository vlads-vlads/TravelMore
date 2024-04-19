package com.example.TravelMore.trip;

import com.example.TravelMore.Comment.Comment;
import com.example.TravelMore.Comment.CommentService;
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
import java.util.Set;

@RestController
@RequestMapping("/api/trips")
public class TripRestController {

    private final TripService tripService;
//    private final TripParticipantService tripParticipantService;
    private final CommentService commentService;

    @Autowired
    public TripRestController(TripService tripService, CommentService commentService) {
        this.tripService = tripService;
//        this.tripParticipantService = tripParticipantService;
        this.commentService = commentService;
    }

//    @PostMapping("/add")
//    public ResponseEntity<Trip> addTrip(@Valid @RequestBody Trip trip) {
//        Trip addedTrip = tripService.addTrip(trip);
//        return new ResponseEntity<>(addedTrip, HttpStatus.CREATED);
//    }
//
//    @DeleteMapping("/{tripId}")
//    public ResponseEntity<Trip> removeTripById(@PathVariable Long tripId) {
//        Trip removedTrip = tripService.removeTripById(tripId);
//        return new ResponseEntity<>(removedTrip, HttpStatus.OK);
//    }

//    @PutMapping("/{tripId}")
//    public ResponseEntity<Trip> updateTrip(@PathVariable Long tripId, @Valid @RequestBody Trip updatedTrip) {
//        Trip updated = tripService.updateTrip(tripId, updatedTrip);
//        return new ResponseEntity<>(updated, HttpStatus.OK);
//    }

//    @PostMapping("/{tripId}/participants")
//    public ResponseEntity<Trip> addParticipantToTrip(@PathVariable Long tripId, @Valid @RequestBody User participant) {
//        Trip updatedTrip = tripService.addParticipantToTrip(tripId, participant);
//        return new ResponseEntity<>(updatedTrip, HttpStatus.OK);
//    }

    @GetMapping("/all")
    public ResponseEntity<List<Trip>> getAllTrips() {
        List<Trip> trips = tripService.getAllTrips();
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Map<String, Object>> showTripDetails(@PathVariable Long tripId) {
        Trip trip = tripService.getTripById(tripId);

        if (trip == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Set<User> participants = tripService.getTripParticipants(tripId);
        List<Comment> comments = commentService.getCommentsForTrip(tripId);

        Map<String, Object> tripDetails = new HashMap<>();
        tripDetails.put("trip", trip);
        tripDetails.put("participants", participants);
        tripDetails.put("comments", comments);

        return new ResponseEntity<>(tripDetails, HttpStatus.OK);
    }

}
