package com.example.TravelMore.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload/{tripId}")
    public String uploadImageToTrip(@RequestParam("file") MultipartFile file, @PathVariable("tripId") Long tripId, RedirectAttributes redirectAttributes) {
        try {
            imageService.uploadImageToTrip(file, tripId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error uploading image: " + e.getMessage());
        }
        return "redirect:/trip/" + tripId;
    }

    @GetMapping("/view/{tripId}")
    public String viewTripImages(@PathVariable Long tripId, Model model) {
        List<Image> images = imageService.getImagesByTripId(tripId);
        model.addAttribute("images", images);
        return "trip_images";
    }
}
