package com.example.TravelMore.Image;

import com.example.TravelMore.util.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ImageService {
    ImageRepository imageRepository;

    @Autowired
    ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image saveImage(MultipartFile file) {
        Image image;
        try {
            String encodedString = Base64Util.encodeToString(file.getBytes());
            image = new Image(encodedString, file.getContentType());
            imageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }
}
