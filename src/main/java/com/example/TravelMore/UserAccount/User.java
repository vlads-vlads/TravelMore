package com.example.TravelMore.UserAccount;

import com.example.TravelMore.joinRequest.JoinRequest;
import com.example.TravelMore.trip.Trip;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )

    @Column(name = "user_id")
    private Long id;

    private String userName;

    private String userEmail;

    private String userPassword;

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
    private List<JoinRequest> sentJoinRequests;

    @OneToMany(mappedBy = "creator")
    private Set<Trip> createdTrips = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    private Set<Trip> participatedTrips = new HashSet<>();

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

    public Set<Trip> getCreatedTrips() {
        return createdTrips;
    }

    public void setCreatedTrips(Set<Trip> createdTrips) {
        this.createdTrips = createdTrips;
    }

    public Set<Trip> getParticipatedTrips() {
        return participatedTrips;
    }

    public void setParticipatedTrips(Set<Trip> participatedTrips) {
        this.participatedTrips = participatedTrips;
    }
}
