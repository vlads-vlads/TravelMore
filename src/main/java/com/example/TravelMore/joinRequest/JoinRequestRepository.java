package com.example.TravelMore.joinRequest;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {

    List<JoinRequest> findByTrip(Trip trip);

    List<JoinRequest> findByUser(User user);

    List<JoinRequest> findByTripId(Long tripId);
}
