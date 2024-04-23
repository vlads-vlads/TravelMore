package com.example.TravelMore.trip;

import com.example.TravelMore.Comment.Comment;
import com.example.TravelMore.Image.Image;
import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.joinRequest.JoinRequest;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Entity
@Table
public class Trip {

    @Id
    @SequenceGenerator(name = "trip_sequence", sequenceName = "trip_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_sequence")
    private Long id;

    @Column(length = 4000)
    private String description;

    private String imageUrl;

    private boolean isAPost;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    private String destination;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @ManyToMany
    @JoinTable(
            name = "trip_participants",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants = new HashSet<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<JoinRequest> joinRequests = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public Trip() {
    }

    public Trip(User creator, String destination, Date startDate, Date endDate, String description) {

    }
    public Trip(List<Comment> comments) {
        this.comments = comments;
    }

    public Trip(User creator, String destination, Date startDate, Date endDate, String description, List<Comment> comments) {
        this.creator = creator;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.comments = comments;
    }

    public Trip(User creator, String destination, Date startDate, Date endDate, Set<User> participants, String description, List<Comment> comments) {
        this.creator = creator;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = participants;
        this.description = description;
        this.comments = comments;
    }

    public boolean isAPost() {
        return isAPost;
    }

    public void setAPost(boolean APost) {
        isAPost = APost;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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

    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public long getDuration() {
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<JoinRequest> getJoinRequests() {
        return joinRequests;
    }

    public void setJoinRequests(List<JoinRequest> joinRequests) {
        this.joinRequests = joinRequests;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

