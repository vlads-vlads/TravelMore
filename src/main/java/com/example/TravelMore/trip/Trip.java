package com.example.TravelMore.trip;

import com.example.TravelMore.Image.Image;
import com.example.TravelMore.UserAccount.User;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    @JoinColumn(name = "user_id")
    private User creator;

    private String destination;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "trip_participants", joinColumns = @JoinColumn(name = "trip_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> participants;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<Image> images;


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

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
