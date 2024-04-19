package com.example.TravelMore.trip;

import com.example.TravelMore.Comment.CommentService;
import com.example.TravelMore.Image.ImageService;
import com.example.TravelMore.UserAccount.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;
//    private final TripParticipantService tripParticipantService;
    private final CommentService commentService;
    private final ImageService imageService;

    @Autowired
    public TripController(TripService tripService, CommentService commentService, ImageService imageService) {
        this.tripService = tripService;
//        this.tripParticipantService = tripParticipantService;
        this.commentService = commentService;
        this.imageService = imageService;
    }

    @GetMapping("/all")
    public String showAllTrips(Model model) {
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
    public String addTrip(@Valid @ModelAttribute("trip") Trip trip,
                          @RequestParam("startDate") String startDateString,
                          @RequestParam("endDate") String endDateString,
                          @RequestParam("file") MultipartFile file,
                          @RequestParam("description") String description,
                          Model model) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);


            trip.setStartDate(startDate);
            trip.setEndDate(endDate);
            trip.setDescription(description);

            tripService.addTrip(trip);

            if (!file.isEmpty()) {
                imageService.uploadPhoto(file, trip.getId());
            }

            return "redirect:/main";
        } catch (ParseException | IOException e) {
            model.addAttribute("error", "Failed to add trip");
            return "error";
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

    @PostMapping("/{id}/remove")
    public String deleteTrip(@PathVariable("id") Long tripId) {
        try {
            tripService.removeTripById(tripId);
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

    @GetMapping("/{tripId}/participants")
    public String getTripParticipants(@PathVariable Long tripId, Model model) {
        Set<User> participants = tripService.getTripParticipants(tripId);
        model.addAttribute("participants", participants);
        return "tripParticipants"; // Return the view name
    }
}
