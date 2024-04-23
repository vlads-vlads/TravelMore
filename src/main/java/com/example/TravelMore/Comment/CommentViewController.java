package com.example.TravelMore.Comment;

import com.example.TravelMore.trip.TripService;
import com.example.TravelMore.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentViewController {

    private final CommentService commentService;
    private final JwtTokenUtil jwtTokenUtil;
    private final TripService tripService;

    @Autowired
    public CommentViewController(CommentService commentService, JwtTokenUtil jwtTokenUtil, TripService tripService) {
        this.commentService = commentService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tripService = tripService;
    }

    @GetMapping
    public String showAllComments(Model model) {
        model.addAttribute("comments", commentService.getAllComments());
        return "comment-list";
    }

    @GetMapping("/{id}")
    public String showCommentById(@PathVariable Long id, Model model) {
        return commentService.getCommentById(id)
                .map(comment -> {
                    model.addAttribute("comment", comment);
                    return "comment-detail";
                })
                .orElse("redirect:/comments");
    }

    @GetMapping("/trip/{tripId}/create")
    public String showCreateCommentForTrip(@PathVariable Long tripId, Model model) {
        model.addAttribute("comment", new Comment());
        model.addAttribute("tripId", tripId);
        return "comment-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditComment(@PathVariable Long id, Model model) {
        return commentService.getCommentById(id)
                .map(comment -> {
                    model.addAttribute("comment", comment);
                    return "comment-edit";
                })
                .orElse("redirect:/comments");
    }

    @GetMapping("/{id}/delete")
    public String showDeleteComment(@PathVariable Long id, Model model) {
        return commentService.getCommentById(id)
                .map(comment -> {
                    model.addAttribute("comment", comment);
                    return "comment-delete";
                })
                .orElse("redirect:/comments");
    }

    @PostMapping("/comment/add")
    public String addComment(@RequestParam("tripId") Long tripId, @RequestParam("description") String description, @CookieValue(value = "authToken", defaultValue = "") String authToken, Model model) {
        try {
            if (!authToken.isEmpty()) {
                Long userId = jwtTokenUtil.extractUserId(authToken);
                if (userId != null && jwtTokenUtil.validateToken(authToken, userId)) {
                    if (!description.isBlank()) {
                        Comment comment = new Comment();
                        comment.setContent(description);
                        commentService.saveComment(comment, tripId, userId);
                    }
                }
                return "redirect:/tripcard?tripId=" + tripId + "&userId=" + userId;
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add trip" + e.getMessage());
            return "errorPage";
        }
        return "redirect:/index";
    }
}


