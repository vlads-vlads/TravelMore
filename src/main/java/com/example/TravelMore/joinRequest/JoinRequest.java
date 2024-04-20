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
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    private JoinRequestStatus status;

    public JoinRequest() {
    }

    public JoinRequest(Trip trip, User requester, User receiver, JoinRequestStatus status) {
        this.trip = trip;
        this.requester = requester;
        this.receiver = receiver;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public JoinRequestStatus getStatus() {
        return status;
    }

    public void setStatus(JoinRequestStatus status) {
        this.status = status;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
