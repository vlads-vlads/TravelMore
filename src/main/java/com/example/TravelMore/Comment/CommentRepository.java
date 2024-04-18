package com.example.TravelMore.Comment;

import com.example.TravelMore.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    static void deleteByTrip(Trip trip) {
    }
    List<Comment> findByTripId(Long tripId);
}
