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
    private final UserService userService;

    @Autowired
    public TripParticipantController(TripParticipantService tripParticipantService, TripService tripService, UserService userService) {
        this.tripParticipantService = tripParticipantService;
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping("/{tripId}/add")
    public String showAddParticipantForm(@PathVariable Long tripId, Model model) {
        Trip trip = tripService.getTripById(tripId);
        List<User> users = userService.getUsers(); // Use UserService to get all users
        model.addAttribute("trip", trip);
        model.addAttribute("users", users);
        return "addParticipant"; // Assuming you have a view named "addParticipant"
    }

    @PostMapping("/{tripId}/add")
    public String addParticipantToTrip(@PathVariable Long tripId, @RequestParam Long userId) {
        Trip trip = tripService.getTripById(tripId);
        User participant = userService.getUserById(userId); // Use UserService to get user by ID
        tripParticipantService.addParticipantToTrip(trip, participant);
        return "redirect:/trip-participants/" + tripId + "/participants"; // Redirect to the list of participants
    }

    @GetMapping("/{tripId}/participants")
    public String showTripParticipants(@PathVariable Long tripId, Model model) {
        Trip trip = tripService.getTripById(tripId);
        List<TripParticipant> participants = tripParticipantService.getParticipantsByTrip(trip);
        model.addAttribute("trip", trip);
        model.addAttribute("participants", participants);
        return "tripParticipants"; // Assuming you have a view named "tripParticipants"
    }

}
