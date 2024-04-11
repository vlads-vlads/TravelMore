package com.example.TravelMore.Comment;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final com.example.TravelMore.Comment.CommentRepository commentRepository;
    private final PostService postService;

    @Autowired
    public CommentService(com.example.TravelMore.Comment.CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }


    public Comment saveComment(@Valid Comment comment, Long postId) {
        LocalDateTime now = LocalDateTime.now();
        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(now);
        }
        comment.setLastModifiedAt(now);
        Post post = postService.getPostById(postId);
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Long id) {
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

    public List<Comment> getCommentsForPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}
