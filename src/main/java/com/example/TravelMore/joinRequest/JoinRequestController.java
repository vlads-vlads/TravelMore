package com.example.TravelMore.joinRequest;

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

    @Autowired
    public JoinRequestController(JoinRequestService joinRequestService) {
        this.joinRequestService = joinRequestService;
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
    public String sendJoinRequestToUser(@RequestParam Long tripId, @RequestParam Long tripCreatorId, @RequestParam Long receiverUserId, RedirectAttributes redirectAttributes) {
        joinRequestService.sendJoinRequestToUser(tripId, tripCreatorId, receiverUserId);
        // Add a success message or any other attributes to be displayed on the redirected page
        redirectAttributes.addFlashAttribute("message", "Join request sent successfully.");
        return "redirect:/main"; // Redirect to home page or any other appropriate view
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
