package com.example.TravelMore.trip;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/all")
    public String getAllTrips(Model model) {
        List<Trip> trips = tripService.getAllTrips();
        model.addAttribute("trips", trips);
        return "allTrips";
    }

    @GetMapping("/add")
    public String getAddTripForm(Model model) {
        model.addAttribute("trip", new Trip());
        return "addTrip";
    }

    @PostMapping("/add")
    public String addTrip(@Valid @ModelAttribute("trip") Trip trip) {
        tripService.addTrip(trip);
        return "redirect:/trips/all";
    }

    @GetMapping("/{tripId}/remove")
    public String getRemoveTripForm(@PathVariable Long tripId, Model model) {
        Trip trip = tripService.removeTripById(tripId);
        model.addAttribute("trip", trip);
        return "removeTrip";
    }

    @PostMapping("/{tripId}/remove")
    public String removeTrip(@PathVariable Long tripId) {
        tripService.getTripById(tripId);
        return "redirect:/trips/all";
    }
}
