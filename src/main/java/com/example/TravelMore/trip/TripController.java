package com.example.TravelMore.trip;

import com.example.TravelMore.Comment.Comment;
import com.example.TravelMore.Comment.CommentService;
import com.example.TravelMore.Image.ImageService;
import com.example.TravelMore.tripParticipant.TripParticipant;
import com.example.TravelMore.tripParticipant.TripParticipantService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;
    private final TripParticipantService tripParticipantService;
    private final CommentService commentService;

    @Autowired
    private ImageService imageService;

    @Autowired
    public TripController(TripService tripService, TripParticipantService tripParticipantService, CommentService commentService) {
        this.tripService = tripService;
        this.tripParticipantService = tripParticipantService;
        this.commentService = commentService;
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
    public String addTrip(@Valid @ModelAttribute("trip") Trip trip,
                          @RequestParam("startDate") String startDateString,
                          @RequestParam("endDate") String endDateString,
                          @RequestParam("file") MultipartFile file,
                          Model model) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);

            trip.setStartDate(startDate);
            trip.setEndDate(endDate);

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

    @GetMapping("/{tripId}")
    public String showTripDetails(@PathVariable Long tripId, Model model) {
        Trip trip = tripService.getTripById(tripId);

        if (trip == null) {
            return "tripNotFound"; //todo create a view for this case
        }

        List<TripParticipant> participants = tripParticipantService.getParticipantsByTrip(trip);
        List<Comment> comments = commentService.getCommentsForTrip(tripId);

        model.addAttribute("trip", trip);
        model.addAttribute("participants", participants);
        model.addAttribute("comments", comments);

        return "tripDetails";
    }
}
