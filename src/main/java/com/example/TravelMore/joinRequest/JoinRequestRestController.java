package com.example.TravelMore.joinRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/join-requests")
public class JoinRequestRestController {

    private final JoinRequestService joinRequestService;

    @Autowired
    public JoinRequestRestController(JoinRequestService joinRequestService) {
        this.joinRequestService = joinRequestService;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendJoinRequest(@RequestParam Long tripId, @RequestParam Long userId) {
        joinRequestService.sendJoinRequest(tripId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/process")
    public ResponseEntity<Void> processJoinRequest(@RequestParam Long requestId, @RequestParam JoinRequestStatus status) {
        joinRequestService.processJoinRequest(requestId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<JoinRequest>> getJoinRequestsForUser(@PathVariable Long userId) {
        List<JoinRequest> joinRequests = joinRequestService.getJoinRequestsForUserRequester(userId);
        return ResponseEntity.ok(joinRequests);
    }

    @PostMapping("/send-to-user")
    public ResponseEntity<Void> sendJoinRequestToUser(@RequestParam Long tripId, @RequestParam Long tripCreatorId, @RequestParam Long receiverUserId) {
        joinRequestService.sendJoinRequestToUser(tripId, tripCreatorId, receiverUserId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<JoinRequest>> getJoinRequestsForTrip(@PathVariable Long tripId) {
        List<JoinRequest> joinRequests = joinRequestService.getJoinRequestsForTrip(tripId);
        return ResponseEntity.ok(joinRequests);
    }
}
