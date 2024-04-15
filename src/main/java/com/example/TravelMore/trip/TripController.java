package com.example.TravelMore.trip;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public String addTrip(@Valid @ModelAttribute("trip") Trip trip, @RequestParam("startDate") String startDateString, @RequestParam("endDate") String endDateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);

            trip.setStartDate(startDate);
            trip.setEndDate(endDate);

            tripService.addTrip(trip);
            return "redirect:http://localhost:8080/main";
        } catch (ParseException e) {
            return "error";
        }
    }

    @GetMapping("/{tripId}/remove")
    public String getRemoveTripForm(@PathVariable Long tripId, Model model) {
        Trip trip = tripService.removeTripById(tripId);
        model.addAttribute("trip", trip);
        return "removeTrip";
    }

    @PostMapping("/{tripId}/remove")
    public String removeTrip(@PathVariable Long tripId, HttpServletResponse response) {
        tripService.removeTripById(tripId);
        return "redirect:http://localhost:8080/trips/all";
    }
}
