package com.example.TravelMore.joinRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {

    List<JoinRequest> findByRequesterId(Long userId);
    List<JoinRequest> findByTripId(Long tripId);
}
