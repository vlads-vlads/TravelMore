package com.example.TravelMore.tripParticipant;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserService;
import com.example.TravelMore.trip.Trip;
import com.example.TravelMore.trip.TripService;
import com.example.TravelMore.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Controller
//@RequestMapping("/trip-participants")
public class TripParticipantController {

//    private final TripParticipantService tripParticipantService;
//    private final TripService tripService;
//    private final UserService userService;
//    private final JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    public TripParticipantController(TripParticipantService tripParticipantService, TripService tripService, UserService userService, JwtTokenUtil jwtTokenUtil) {
//        this.tripParticipantService = tripParticipantService;
//        this.tripService = tripService;
//        this.userService = userService;
//        this.jwtTokenUtil = jwtTokenUtil;
//    }
//
//    @GetMapping("/{tripId}/add")
//    public String showAddParticipantForm(@PathVariable Long tripId, Model model) {
//        Trip trip = tripService.getTripById(tripId);
//        model.addAttribute("trip", trip);
//        return "addParticipant";
//    }
//
//    @PostMapping("/{tripId}/add")
//    public String addParticipantToTrip(@PathVariable Long tripId, @RequestParam Long participantId) {
//        tripParticipantService.addParticipantToTrip(tripId, participantId);
//        return "redirect:/trip-participants/" + tripId + "/participants";
//    }
//
//    @GetMapping("/{tripId}/participants")
//    public String showTripParticipants(@PathVariable Long tripId, Model model) {
//        Trip trip = tripService.getTripById(tripId);
//        List<TripParticipant> participants = tripParticipantService.getParticipantsByTrip(trip);
//        model.addAttribute("trip", trip);
//        model.addAttribute("participants", participants);
//        return "tripParticipants";
//    }
//
//    @GetMapping("/{tripId}/remove/{participantId}")
//    public String removeParticipantFromTrip(@PathVariable Long tripId, @PathVariable Long participantId) {
//        tripParticipantService.removeParticipantFromTrip(tripId, participantId);
//        return "redirect:/trip-participants/" + tripId + "/participants";
//    }
//
//    @GetMapping("/user/trips")
//    public String getUserTrips(@CookieValue(value = "authToken", defaultValue = "") String authToken, Model model) {
//        Long userId = jwtTokenUtil.extractUserId(authToken);
//        User user = userService.getUserById(userId);
//        List<TripParticipant> userTrips = tripParticipantService.getTripsByParticipant(user);
//        model.addAttribute("userTrips", userTrips);
//
//        return "user-trips";
//    }
}
