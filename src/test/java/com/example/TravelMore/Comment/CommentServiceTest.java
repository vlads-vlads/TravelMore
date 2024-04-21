package com.example.TravelMore.Comment;

import com.example.TravelMore.trip.Trip;
import com.example.TravelMore.trip.TripService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TripService tripService;

    @InjectMocks
    private CommentService commentService;

    @Test
    void testSaveComment() {
        // Arrange
        Comment comment = new Comment();
        Long tripId = 1L;
        Long userId = 1L;
        Trip trip = new Trip();
        when(tripService.getTripById(tripId)).thenReturn(trip);
        when(commentRepository.save(comment)).thenReturn(comment);

        // Act
        Comment result = commentService.saveComment(comment, tripId, userId); // Pass userId

        // Assert
        assertNotNull(result);
        assertEquals(trip, result.getTrip());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testSaveComment_InvalidTripId() {
        // Arrange
        Comment comment = new Comment();
        Long userId = 1L;
        Long tripId = 1L;
        when(tripService.getTripById(tripId)).thenThrow(new IllegalArgumentException());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> commentService.saveComment(comment, tripId, userId));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testGetAllComments() {
        // Arrange
        List<Comment> expectedComments = new ArrayList<>();
        when(commentRepository.findAll()).thenReturn(expectedComments);

        // Act
        List<Comment> result = commentService.getAllComments();

        // Assert
        assertEquals(expectedComments, result);
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void testGetCommentById() {
        // Arrange
        Long commentId = 1L;
        Comment expectedComment = new Comment();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(expectedComment));

        // Act
        Optional<Comment> result = commentService.getCommentById(commentId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedComment, result.get());
        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void testGetCommentById_NotFound() {
        // Arrange
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act
        Optional<Comment> result = commentService.getCommentById(commentId);

        // Assert
        assertFalse(result.isPresent());
        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void testDeleteComment() {
        // Arrange
        Long commentId = 1L;
        Comment comment = new Comment();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        commentService.deleteComment(commentId);

        // Assert
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).save(comment);
        assertNotNull(comment.getLastModifiedAt());
    }

    @Test
    void testDeleteComment_NotFound() {
        // Arrange
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act
        commentService.deleteComment(commentId);

        // Assert
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testUpdateComment() {
        // Arrange
        Comment comment = new Comment();

        // Act
        commentService.updateComment(comment);

        // Assert
        verify(commentRepository, times(1)).save(comment);
        assertNotNull(comment.getLastModifiedAt());
    }

    @Test
    void testGetCommentsForTrip() {
        // Arrange
        Long tripId = 1L;
        List<Comment> expectedComments = new ArrayList<>();
        when(commentRepository.findByTripId(tripId)).thenReturn(expectedComments);

        // Act
        List<Comment> result = commentService.getCommentsForTrip(tripId);

        // Assert
        assertEquals(expectedComments, result);
        verify(commentRepository, times(1)).findByTripId(tripId);
    }
}