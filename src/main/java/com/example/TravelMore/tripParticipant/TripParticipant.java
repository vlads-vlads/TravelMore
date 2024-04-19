package com.example.TravelMore.tripParticipant;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.trip.Trip;
import jakarta.persistence.*;

//@Entity
//@Table(name = "trip_participants")
public class TripParticipant {

//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_participant_generator")
//    @SequenceGenerator(name = "trip_participant_generator", sequenceName = "trip_participant_seq", allocationSize = 1)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "trip_id", referencedColumnName = "id")
//    private Trip trip;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
//    private User user;
//
//    public TripParticipant() {
//    }
//
//    public TripParticipant(Trip trip, User user) {
//        this.trip = trip;
//        this.user = user;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Trip getTrip() {
//        return trip;
//    }
//
//    public void setTrip(Trip trip) {
//        this.trip = trip;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
}
