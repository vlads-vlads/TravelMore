package com.example.TravelMore.Image;

import com.example.TravelMore.trip.Trip;
import com.example.TravelMore.trip.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/images")
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
