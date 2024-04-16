package com.example.TravelMore.trip;

import com.example.TravelMore.UserAccount.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByCreator(User creator);

}
