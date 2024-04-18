package com.example.TravelMore.joinRequest;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.trip.Trip;
import jakarta.persistence.*;

@Entity
@Table(name = "join_requests")
public class JoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "join_request_generator")
    @SequenceGenerator(name = "join_request_generator", sequenceName = "join_request_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    public JoinRequest() {
    }

    public JoinRequest(User user, Trip trip, RequestStatus status) {
        this.user = user;
        this.trip = trip;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
