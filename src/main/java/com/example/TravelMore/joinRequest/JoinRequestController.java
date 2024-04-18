package com.example.TravelMore.joinRequest;

import com.example.TravelMore.UserAccount.UserService;
import com.example.TravelMore.trip.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/join-requests")
public class JoinRequestController {

    private final JoinRequestService joinRequestService;
    private final UserService userService;
    private final TripService tripService;

    @Autowired
    public JoinRequestController(JoinRequestService joinRequestService, UserService userService, TripService tripService) {
        this.joinRequestService = joinRequestService;
        this.userService = userService;
        this.tripService = tripService;
    }

    @PostMapping("/create")
    public String createJoinRequest(@RequestParam Long userId, @RequestParam Long tripId) {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUser(userService.getUserById(userId));
        joinRequest.setTrip(tripService.getTripById(tripId));
        joinRequestService.createJoinRequest(joinRequest);
        return "redirect:/trips/" + joinRequest.getTrip().getId();
    }

    @GetMapping("/viewByTrip/{tripId}")
    public String viewJoinRequestsByTripId(@PathVariable Long tripId, Model model) {
        List<JoinRequest> joinRequests = joinRequestService.getJoinRequestsByTripId(tripId);
        model.addAttribute("joinRequests", joinRequests);
        return "joinRequestList";
    }

    @GetMapping("/viewByUser/{userId}")
    public String viewJoinRequestsByUser(@PathVariable Long userId, Model model) {
        List<JoinRequest> joinRequests = joinRequestService.getJoinRequestsByUserId(userId);
        model.addAttribute("joinRequests", joinRequests);
        // Return the view to display join requests for the user
        return "joinRequestList";
    }

    @PostMapping("/{joinRequestId}/accept")
    public String acceptJoinRequest(@PathVariable Long joinRequestId, RedirectAttributes redirectAttributes) {
        try {
            JoinRequest joinRequest = joinRequestService.getJoinRequestById(joinRequestId);
            if (joinRequest != null) {
                joinRequestService.acceptJoinRequest(joinRequestId);
                return "redirect:/trips/" + joinRequest.getTrip().getId();
            } else {
                redirectAttributes.addFlashAttribute("error", "Join request not found.");
                return "redirect:/error";
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error";
        }
    }

    @PostMapping("/{joinRequestId}/decline")
    public String declineJoinRequest(@PathVariable Long joinRequestId, RedirectAttributes redirectAttributes) {
        try {
            JoinRequest joinRequest = joinRequestService.getJoinRequestById(joinRequestId);
            if (joinRequest != null) {
                joinRequestService.declineJoinRequest(joinRequestId);
                return "redirect:/trips/" + joinRequest.getTrip().getId();
            } else {
                redirectAttributes.addFlashAttribute("error", "Join request not found.");
                return "redirect:/error";
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error";
        }
    }
}
