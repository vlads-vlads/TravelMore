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
    private final TripService tripService;

    @Autowired
    public JoinRequestController(JoinRequestService joinRequestService, TripService tripService) {
        this.joinRequestService = joinRequestService;
        this.tripService = tripService;
    }

    @GetMapping("/user/{userId}")
    public String getJoinRequestsForUser(@PathVariable Long userId, Model model) {
        List<JoinRequest> joinRequests = joinRequestService.getJoinRequestsForUserRequester(userId);
        model.addAttribute("joinRequests", joinRequests);
        return "joinRequests";
    }

    @PostMapping("/send")
    public String sendJoinRequest(@RequestParam Long tripId, @RequestParam Long userId, Model model) {
        try {
            joinRequestService.sendJoinRequest(tripId, userId);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to send join request: " + e.getMessage());
            return "errorPage";
        }
    }

    @PostMapping("/process")
    public String processJoinRequest(@RequestParam Long requestId, @RequestParam JoinRequestStatus status, Model model) {
        try {
            joinRequestService.processJoinRequest(requestId, status);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to change status of join request: " + e.getMessage());
            return "errorPage";
        }
    }

    @PostMapping("/send-to-user")
    public String sendJoinRequestToUser(@RequestParam Long tripId, @RequestParam Long receiver, @RequestParam Long
            requester, RedirectAttributes redirectAttributes) {
        try {
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
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to send join request to user: " + e.getMessage());
            return "errorPage";
        }
    }

    @GetMapping("/{tripId}/join-requests")
    public String getJoinRequestsForTrip(@PathVariable Long tripId, Model model) {
        List<JoinRequest> joinRequests = joinRequestService.getJoinRequestsForTrip(tripId);
        model.addAttribute("joinRequests", joinRequests);
        return "joinRequests";
    }

    @PostMapping("/approve")
    public String approveJoinRequest(@RequestParam("requestId") Long requestId, RedirectAttributes
            redirectAttributes) {
        try {
            joinRequestService.approveJoinRequest(requestId);
            redirectAttributes.addFlashAttribute("successMessage", "Join request approved successfully");
            return "redirect:/offers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to approve join request: " + e.getMessage());
            return "errorPage";
        }
    }

    @PostMapping("/decline")
    public String declineJoinRequest(@RequestParam("requestId") Long requestId, RedirectAttributes
            redirectAttributes) {
        try {
            joinRequestService.declineJoinRequest(requestId);
            redirectAttributes.addFlashAttribute("successMessage", "Join request declined successfully");
            return "redirect:/offers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to decline join request: " + e.getMessage());
            return "errorPage";
        }
    }

}
