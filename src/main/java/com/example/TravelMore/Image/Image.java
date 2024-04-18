package com.example.TravelMore.Image;

import com.example.TravelMore.trip.Trip;
import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_sequence_generator")
    @SequenceGenerator(name = "image_sequence_generator", sequenceName = "image_seq", allocationSize = 1)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private String base64image;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Image() {
    }

    public Image(String base64image, Trip trip) {
        this.base64image = base64image;
        this.trip = trip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBase64image() {
        return base64image;
    }

    public void setBase64image(String base64image) {
        this.base64image = base64image;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
