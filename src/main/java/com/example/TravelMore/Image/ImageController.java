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
