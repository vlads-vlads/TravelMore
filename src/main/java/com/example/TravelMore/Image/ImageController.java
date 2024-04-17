package com.example.TravelMore.Image;

import com.example.TravelMore.trip.Trip;
import com.example.TravelMore.trip.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private TripService tripService;

    @PostMapping("/upload-photo")
    public String uploadPhoto(@RequestParam("file") MultipartFile file, @RequestParam("tripId") Long tripId, Model model) {
        try {
            if (file.isEmpty()) {
                model.addAttribute("error", "File is empty");
                return "uploadError";
            }


            if (!file.isEmpty()) {
                imageService.uploadPhoto(file, tripId);
            }

            model.addAttribute("message", "Photo uploaded successfully");
            return "uploadSuccess";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to upload photo");
            return "uploadError";
        }
    }

    @GetMapping("/trip/{tripId}/photos")
    public String getAllPhotosForTrip(@PathVariable Trip trip, Model model) {
        List<Image> photos = imageService.getImagesByTrip(trip);
        if (photos == null) {
            model.addAttribute("error", "Photos not found for the trip");
            return "error";
        }

        model.addAttribute("photos", photos);
        return "photoList";
    }
}