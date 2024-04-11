package com.example.TravelMore.trip;

import com.example.TravelMore.UserAccount.User;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table
public class Trip {

    @Id
    @SequenceGenerator(name = "trip_sequence", sequenceName = "trip_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_sequence")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    private String destination;

    private Date startDate;

    private Date endDate;

    @ManyToMany
    @JoinTable(name = "trip_participants", joinColumns = @JoinColumn(name = "trip_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> participants;


/*
    private List<Photo> photos;
    private List<Comment> comments;
*/

    public Trip() {
    }

    public Trip(String name, User creator, String destination, Date startDate, Date endDate) {
        this.name = name;
        this.creator = creator;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Trip(String name, User creator, String destination, Date startDate, Date endDate, List<User> participants) {
        this.name = name;
        this.creator = creator;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = participants;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }
}
