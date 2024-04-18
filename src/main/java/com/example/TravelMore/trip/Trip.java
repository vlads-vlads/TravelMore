package com.example.TravelMore.trip;

import com.example.TravelMore.Image.Image;
import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.joinRequest.JoinRequest;
import com.example.TravelMore.tripParticipant.TripParticipant;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_sequence_generator")
    @SequenceGenerator(name = "trip_sequence_generator", sequenceName = "trip_seq", allocationSize = 1)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    private String destination;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<JoinRequest> joinRequests = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<TripParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<Image> images;

    public Trip() {
    }

    public Trip(User creator, String destination, Date startDate, Date endDate) {
        this.creator = creator;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addJoinRequest(JoinRequest joinRequest) {
        this.joinRequests.add(joinRequest);
        joinRequest.setTrip(this);
    }

    public void removeJoinRequest(JoinRequest joinRequest) {
        this.joinRequests.remove(joinRequest);
        joinRequest.setTrip(null);
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<TripParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<TripParticipant> participants) {
        this.participants = participants;
    }

    public List<JoinRequest> getJoinRequests() {
        return joinRequests;
    }

    public void setJoinRequests(List<JoinRequest> joinRequests) {
        this.joinRequests = joinRequests;
    }

    public long getDuration() {
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
    }
}
