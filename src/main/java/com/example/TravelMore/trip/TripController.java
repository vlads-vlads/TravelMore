package com.example.TravelMore.trip;

import com.example.TravelMore.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public TripController(TripService tripService, JwtTokenUtil jwtTokenUtil) {
        this.tripService = tripService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/create")
    public String showCreateTripForm(Model model) {
        model.addAttribute("trip", new Trip());
        return "createTripForm";
    }

    @PostMapping("/create")
    public String createTrip(@ModelAttribute("trip") Trip trip, @RequestHeader(name = "Authorization") String token) {
        Long creatorId = jwtTokenUtil.extractUserId(token);
        try {
            tripService.createTrip(trip, creatorId);
            return "redirect:/trips/all";
        } catch (IllegalArgumentException e) {
            return "errorPage";
        }
    }

    @GetMapping("/{id}/update")
    public String showUpdateTripForm(@PathVariable("id") Long tripId, Model model) {
        Trip trip = tripService.getTripById(tripId);
        if (trip != null) {
            model.addAttribute("trip", trip);
            return "updateTripForm";
        } else {
            return "errorPage";
        }
    }

    @PostMapping("/{id}/update")
    public String updateTrip(@ModelAttribute("trip") Trip trip) {
        try {
            tripService.updateTrip(trip);
            return "redirect:/trips/all";
        } catch (IllegalArgumentException e) {
            return "errorPage";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteTrip(@PathVariable("id") Long tripId) {
        try {
            tripService.deleteTrip(tripId);
            return "redirect:/trips/all";
        } catch (IllegalArgumentException e) {
            return "errorPage";
        }
    }

    @GetMapping("/{id}")
    public String showTripDetails(@PathVariable("id") Long tripId, Model model) {
        Trip trip = tripService.getTripById(tripId);
        if (trip != null) {
            model.addAttribute("trip", trip);
            return "tripDetails";
        } else {
            return "errorPage";
        }
    }

    @GetMapping("/all")
    public String showAllTrips(Model model) {
        List<Trip> trips = tripService.getAllTrips();
        model.addAttribute("trips", trips);
        return "allTrips";
    }

//    @PostMapping("/{tripId}/addParticipant")
//    public String addParticipantToTrip(@PathVariable("tripId") Long tripId, @RequestParam("userId") Long userId) {
//        try {
//            tripService.addParticipantToTrip(tripId, userId);
//            return "redirect:/trips/all";
//        } catch (IllegalArgumentException e) {
//            return "errorPage";
//        }
//    }
//
//    @PostMapping("/{tripId}/removeParticipant")
//    public String removeParticipantFromTrip(@PathVariable("tripId") Long tripId, @RequestParam("participantId") Long participantId) {
//        try {
//            tripService.removeParticipantFromTrip(tripId, participantId);
//            return "redirect:/trips/all";
//        } catch (IllegalArgumentException e) {
//            return "errorPage";
//        }
//    }
}
