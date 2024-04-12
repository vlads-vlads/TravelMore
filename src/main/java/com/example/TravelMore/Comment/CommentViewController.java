package com.example.TravelMore.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comments")
public class CommentViewController {

    private final CommentService commentService;

    @Autowired
    public CommentViewController(CommentService commentService) {
        this.commentService = commentService;
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
}


