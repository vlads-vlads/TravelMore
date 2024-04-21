package com.example.TravelMore.Comment;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserService;
import com.example.TravelMore.trip.Trip;
import com.example.TravelMore.trip.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final TripService tripService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, TripService tripService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.tripService = tripService;
    }

    public Comment saveComment(@Valid Comment comment, Long tripId, Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        comment.setUser(user);
        LocalDateTime now = LocalDateTime.now();
        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(now);
        }
        comment.setLastModifiedAt(now);
        Trip trip = tripService.getTripById(tripId);
        comment.setTrip(trip);
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments() { // retrieves all comm from databse using JPA interface
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Long id) { // retrieved 1 comment by Id, Optional is a cotainer that can be empty or not , if a comm with id exists the method returns comm, if not returns empty
        return commentRepository.findById(id);
    }

    public void deleteComment(Long id) {
        commentRepository.findById(id).ifPresent(comment -> {
            comment.setLastModifiedAt(LocalDateTime.now());
            commentRepository.save(comment);
        });
    }

    public Comment updateComment(Comment comment) {
        comment.setLastModifiedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsForTrip(Long trip_id) {
        return commentRepository.findByTripId(trip_id);
    }
}
