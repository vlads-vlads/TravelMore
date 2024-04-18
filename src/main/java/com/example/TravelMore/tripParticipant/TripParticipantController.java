package com.example.TravelMore.tripParticipant;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserService;
import com.example.TravelMore.trip.Trip;
import com.example.TravelMore.trip.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/trip-participants")
public class TripParticipantController {

    private final TripParticipantService tripParticipantService;
    private final TripService tripService;

    @Autowired
    public TripParticipantController(TripParticipantService tripParticipantService, TripService tripService) {
        this.tripParticipantService = tripParticipantService;
        this.tripService = tripService;
    }

    @GetMapping("/{tripId}/add")
    public String showAddParticipantForm(@PathVariable Long tripId, Model model) {
//        Trip trip = tripService.getTripById(tripId);
//        List<User> users = tripService.getAvailableParticipants(tripId);
//        model.addAttribute("trip", trip);
//        model.addAttribute("users", users);
        return "addParticipant";
    }

    @PostMapping("/{tripId}/add")
    public String addParticipantToTrip(@PathVariable Long tripId, @RequestParam Long participantId) {
        tripParticipantService.addParticipantToTrip(tripId, participantId);
        return "redirect:/trip-participants/" + tripId + "/participants";
    }

    @GetMapping("/{tripId}/participants")
    public String showTripParticipants(@PathVariable Long tripId, Model model) {
        Trip trip = tripService.getTripById(tripId);
        List<TripParticipant> participants = tripParticipantService.getParticipantsByTrip(trip);
        model.addAttribute("trip", trip);
        model.addAttribute("participants", participants);
        return "tripParticipants";
    }

    @GetMapping("/{tripId}/remove/{participantId}")
    public String removeParticipantFromTrip(@PathVariable Long tripId, @PathVariable Long participantId) {
        tripParticipantService.removeParticipantFromTrip(tripId, participantId);
        return "redirect:/trip-participants/" + tripId + "/participants";
    }
}
