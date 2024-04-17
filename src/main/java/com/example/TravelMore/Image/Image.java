package com.example.TravelMore.Image;

import jakarta.persistence.Id;

public class Image {
    @Id
    Long imageId;
    String data;
    String type;
    public Image(String encodedString, String fileType){
        this.data = encodedString;
        this.type = fileType;
    }
}
