package com.example.TravelMore.joinRequest;

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
    private  final TripService tripService;

    @Autowired
    public JoinRequestController(JoinRequestService joinRequestService, TripService tripService) {
        this.joinRequestService = joinRequestService;
        this.tripService = tripService;
    }

    @GetMapping("/user/{userId}")
    public String getJoinRequestsForUser(@PathVariable Long userId, Model model) {
        List<JoinRequest> joinRequests = joinRequestService.getJoinRequestsForUserRequester(userId);
        model.addAttribute("joinRequests", joinRequests);
        return "joinRequests"; // Return the view name
    }

    @PostMapping("/send")
    public String sendJoinRequest(@RequestParam Long tripId, @RequestParam Long userId, Model model) {
        joinRequestService.sendJoinRequest(tripId, userId);
        return "redirect:/"; // Redirect to home page or any other appropriate view
    }

    @PostMapping("/process")
    public String processJoinRequest(@RequestParam Long requestId, @RequestParam JoinRequestStatus status, Model model) {
        joinRequestService.processJoinRequest(requestId, status);
        return "redirect:/"; // Redirect to home page or any other appropriate view
    }

    @PostMapping("/send-to-user")
    public String sendJoinRequestToUser(@RequestParam Long tripId, @RequestParam Long receiver, @RequestParam Long requester, RedirectAttributes redirectAttributes) {
        if (requester.equals(receiver)) {
            redirectAttributes.addFlashAttribute("error", "You cannot send a join request to yourself.");
            return "redirect:/main";
        }

        if (tripService.isUserParticipantInTrip(requester, tripId)) {
            redirectAttributes.addFlashAttribute("error", "You are already a participant in this trip.");
            return "redirect:/main";
        }

        if (joinRequestService.existsJoinRequest(tripId, receiver, requester)) {
            redirectAttributes.addFlashAttribute("error", "A join request has already been sent to this user for this trip.");
            return "redirect:/main";
        }

        joinRequestService.sendJoinRequestToUser(tripId, receiver, requester);
        redirectAttributes.addFlashAttribute("message", "Join request sent successfully.");
        return "redirect:/main";
    }

    @GetMapping("/{tripId}/join-requests")
    public String getJoinRequestsForTrip(@PathVariable Long tripId, Model model) {
        List<JoinRequest> joinRequests = joinRequestService.getJoinRequestsForTrip(tripId);
        model.addAttribute("joinRequests", joinRequests);
        return "joinRequests"; // Return the view name
    }

    @PostMapping("/approve")
    public String approveJoinRequest(@RequestParam("requestId") Long requestId, RedirectAttributes redirectAttributes) {
        joinRequestService.approveJoinRequest(requestId);
        redirectAttributes.addFlashAttribute("successMessage", "Join request approved successfully");
        return "redirect:/offers";
    }

    @PostMapping("/decline")
    public String declineJoinRequest(@RequestParam("requestId") Long requestId, RedirectAttributes redirectAttributes) {
        joinRequestService.declineJoinRequest(requestId);
        redirectAttributes.addFlashAttribute("successMessage", "Join request declined successfully");
        return "redirect:/offers";
    }

}
