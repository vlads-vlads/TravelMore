    package com.example.TravelMore.Image;

    import com.example.TravelMore.trip.Trip;
    import com.example.TravelMore.trip.TripService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.ByteArrayOutputStream;
    import java.io.IOException;
    import java.util.Base64;
    import java.util.List;

    import java.io.ByteArrayInputStream;
    import javax.imageio.ImageIO;

    @Service
    public class ImageService {

        @Autowired
        private ImageRepository imageRepository;

        @Autowired
        private TripService tripService;

        public void uploadPhotos(MultipartFile[] files, Long tripId) throws IOException {
            Trip trip = tripService.getTripById(tripId);
            if (trip != null) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        byte[] imageData = file.getBytes();
                        String base64Image = Base64.getEncoder().encodeToString(imageData);
                        Image photo = new Image(base64Image, trip);
                        imageRepository.save(photo);
                    }
                }
            } else {
                throw new IllegalArgumentException("Trip not found");
            }
        }


        public List<Image> getImagesByTrip(Trip trip) {
            return imageRepository.findByTrip(trip);
        }
    }
