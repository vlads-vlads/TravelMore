package com.example.TravelMore.trip;

import com.example.TravelMore.Comment.CommentService;
import com.example.TravelMore.Image.ImageService;
import com.example.TravelMore.UserAccount.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String addTrip(@Valid @ModelAttribute("trip") com.example.TravelMore.trip.Trip trip,
                          @RequestParam(value = "startDate", required = false) String startDateString,
                          @RequestParam(value = "endDate", required = false) String endDateString,
                          @RequestParam(value = "files[]", required = false) MultipartFile[] files,
                          @RequestParam("description") String description,
                          Model model) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null;
            Date endDate = null;

            if (startDateString != null && !startDateString.trim().isEmpty()) {
                startDate = dateFormat.parse(startDateString);
                trip.setStartDate(startDate);
            }
            if (endDateString != null && !endDateString.trim().isEmpty()) {
                endDate = dateFormat.parse(endDateString);
                trip.setEndDate(endDate);
            }

            trip.setDescription(description);

            com.example.TravelMore.trip.Trip savedTrip = tripService.addTrip(trip);

            if (files != null && files.length > 0) {
                imageService.uploadPhotos(files, savedTrip.getId());
            }

            return "redirect:/main";
        } catch (ParseException | IOException e) {
            model.addAttribute("error", "Failed to add trip");
            return "error";
        }
    }

//    @PostMapping("/add")
//    public String addTrip(@Valid @ModelAttribute("trip") Trip trip,
//                          @RequestParam("startDate") String startDateString,
//                          @RequestParam("endDate") String endDateString,
//                          @RequestParam("files[]") MultipartFile[] files,
//                          @RequestParam("description") String description,
//                          Model model) {
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date startDate = dateFormat.parse(startDateString);
//            Date endDate = dateFormat.parse(endDateString);
//
//            trip.setStartDate(startDate);
//            trip.setEndDate(endDate);
//            trip.setDescription(description);
//
//            tripService.addTrip(trip);
//            imageService.uploadPhotos(files, trip.getId());
//
//            return "redirect:/main";
//        } catch (ParseException | IOException e) {
//            model.addAttribute("error", "Failed to add trip");
//            return "error";
//        }
//    }

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

//    @PostMapping("/{id}/update")
//    public String updateTrip(@ModelAttribute("trip") Trip trip) {
//        try {
//            tripService.updateTrip(trip.g);
//            return "redirect:/trips/all";
//        } catch (IllegalArgumentException e) {
//            return "errorPage";
//        }
//    }
//    @GetMapping("/{tripId}/remove")
//    public String getRemoveTripForm(@PathVariable Long tripId, Model model) {
//        Trip trip = tripService.removeTripById(tripId);
//        model.addAttribute("trip", trip);
//        return "removeTrip";
//    }
//
//    @GetMapping("/{tripId}/remove")
//    public String removeTrip(@PathVariable Long tripId, HttpServletResponse response) {
//        tripService.removeTripById(tripId);
//        return "redirect: /main";
//    }

//    @PostMapping("/{id}/remove")
//    public String deleteTrip(@PathVariable("id") Long tripId) {
//        try {
//            tripService.removeTripById(tripId);
//            return "redirect:/trips/all";
//        } catch (IllegalArgumentException e) {
//            return "errorPage";
//        }
    @PostMapping("/{tripId}/remove")
    public String removeTrip(@PathVariable Long tripId, RedirectAttributes redirectAttributes) {
        try {
            tripService.removeTripById(tripId);
            redirectAttributes.addFlashAttribute("success", "Trip successfully deleted!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting trip: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/main";
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


    // CURRENTLY NOT WORKING (UPDATING TRIPS)
//    @PostMapping ("/{id}/edit")
//    public String updateTrip(@PathVariable Long id,
//                             @RequestParam(value = "destination", required = false) String destination,
//                             @RequestParam(value = "startDate", required = false) String startDate,
//                             @RequestParam(value = "endDate", required = false) String endDate,
//                             @RequestParam(value = "description", required = false) String description,
//                             @RequestParam(value = "file", required = false) MultipartFile file,
//                             @RequestParam("user") Long userId,
//                             Model model) {
//
//        Trip tripToUpdate = tripService.getTripById(id);
//
//        if (tripToUpdate == null) {
//            model.addAttribute("error", "Trip not found");
//            return "error-page";
//        }
//
//        if (!tripToUpdate.getCreator().getId().equals(userId)) {
//            model.addAttribute("error", "You are not authorized to update this trip");
//            return "error-page";
//        }
//
//        Date parsedStartDate = null;
//        Date parsedEndDate = null;
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        try {
//            if (startDate != null) {
//                parsedStartDate = dateFormat.parse(startDate);
//                tripToUpdate.setStartDate(parsedStartDate);
//            }
//            if (endDate != null) {
//                parsedEndDate = dateFormat.parse(endDate);
//                tripToUpdate.setEndDate(parsedEndDate);
//            }
//        } catch (ParseException e) {
//            model.addAttribute("error", "Invalid date format");
//            return "error-page";
//        }
//
//        if (destination != null) {
//            tripToUpdate.setDestination(destination);
//        }
//        if (description != null) {
//            tripToUpdate.setDescription(description);
//        }
//        if (file != null && !file.isEmpty()) {
//            try {
//                imageService.uploadPhoto(file, tripToUpdate.getId());
//            } catch (IOException e) {
//                model.addAttribute("error", "Error uploading image: " + e.getMessage());
//                return "error-page";
//            }
//        }
//
//        try {
//            tripService.addTrip(tripToUpdate);
//        } catch (Exception e) {
//            model.addAttribute("error", "Error saving trip: " + e.getMessage());
//            return "error-page";
//        }
//
//        return "redirect:/main";
//    }
}
