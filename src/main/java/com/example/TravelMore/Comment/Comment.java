package com.example.TravelMore.Comment;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.trip.Trip;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_generator")
    @SequenceGenerator(name = "comment_generator", sequenceName = "comment_seq", allocationSize = 1)
    private long id;

    @Column(length = 500, nullable = false)
    @NotBlank(message = "Content cannot be blank")
    @Size(max = 500, message = "Content must be less than 500 characters")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();


    public Comment() {
    }

    public Comment(String content, User user, Trip trip) {
        this.content = content;
        this.user = user;
        this.trip = trip;
    }

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public String getContent() {return content;}

    public void setContent(String content) {this.content = content;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public Trip getTrip() {return trip;}

    public void setTrip(Trip trip) {this.trip = trip;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public LocalDateTime getLastModifiedAt() {return lastModifiedAt;}

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {this.lastModifiedAt = lastModifiedAt;}

    public boolean isDeleted() {return isDeleted;}

    public void setDeleted(boolean deleted) {isDeleted = deleted;}

    public Comment getParentComment() {return parentComment;}

    public void setParentComment(Comment parentComment) {this.parentComment = parentComment;}

    public List<Comment> getReplies() {return replies;}

    public void setReplies(List<Comment> replies) {this.replies = replies;}

    public void addReply(Comment reply) {replies.add(reply);reply.setParentComment(this);}

    public void removeReply(Comment reply) {replies.remove(reply);reply.setParentComment(null);}
}

