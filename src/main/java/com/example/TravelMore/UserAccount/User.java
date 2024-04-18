package com.example.TravelMore.UserAccount;

import com.example.TravelMore.joinRequest.JoinRequest;
import com.example.TravelMore.trip.Trip;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence_generator")
    @SequenceGenerator(name = "user_sequence_generator", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    private String userName;

    private String userEmail;

    private String userPassword;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Trip> createdTrips = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<JoinRequest> sentJoinRequests = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "trip_participants",
            joinColumns = @JoinColumn(name = "participant_id"),
            inverseJoinColumns = @JoinColumn(name = "trip_id"))
    private List<Trip> trips = new ArrayList<>();


    public User() {
    }

    public User(String userName, String userEmail, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public List<JoinRequest> getSentJoinRequests() {
        return sentJoinRequests;
    }

    public void setSentJoinRequests(List<JoinRequest> sentJoinRequests) {
        this.sentJoinRequests = sentJoinRequests;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public List<Trip> getCreatedTrips() {
        return createdTrips;
    }

    public void setCreatedTrips(List<Trip> createdTrips) {
        this.createdTrips = createdTrips;
    }
}
