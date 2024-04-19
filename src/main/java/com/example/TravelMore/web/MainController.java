package com.example.TravelMore.web;
import com.example.TravelMore.Comment.Comment;
import com.example.TravelMore.Comment.CommentService;
import com.example.TravelMore.Image.Image;
import com.example.TravelMore.Image.ImageService;
import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserService;
import com.example.TravelMore.model.LoginRequest;
import com.example.TravelMore.trip.Trip;
import com.example.TravelMore.trip.TripService;
import com.example.TravelMore.util.JwtTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

@Controller
public class MainController {

    private final UserService userService;
    private final TripService tripService;
    private final CommentService commentService;
    private final JwtTokenUtil jwtTokenUtil;

    private final ImageService imageService;

    @Autowired
    public MainController(UserService userService, TripService tripService, CommentService commentService, JwtTokenUtil jwtTokenUtil, ImageService imageService) {
        this.userService = userService;
        this.tripService = tripService;
        this.commentService = commentService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.imageService = imageService;
    }

    @PostMapping("/main")
    public String login(Model model, HttpServletResponse response, LoginRequest loginRequest) {

        User user = userService.authenticateUser(loginRequest);
        if (user != null) {
            String token = jwtTokenUtil.generateToken(user.getId());
            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setMaxAge(3600);
            cookie.setPath("/");

            response.addCookie(cookie);
        } else {
            model.addAttribute("error", "Invalid username/password");
            return "register";
        }
        User loggedInUser = userService.getUserById(user.getId());

        List<Trip> trips = tripService.getTripsByCreatorId(user);

        trips.sort(Comparator.comparing(Trip::getStartDate));
        model.addAttribute("user", user);
        model.addAttribute("trips", trips);
        model.addAttribute("loggedInUser", loggedInUser);

        return "main";
    }


    @GetMapping("/main")
    public String mainPage(Model model, @CookieValue(value = "authToken", defaultValue = "") String authToken) {
        if (!authToken.isEmpty()) {
            Long authenticatedUserId = jwtTokenUtil.extractUserId(authToken);

            if (authenticatedUserId != null && jwtTokenUtil.validateToken(authToken, authenticatedUserId)) {
                User loggedInUser = userService.getUserById(authenticatedUserId);

                User user = userService.getUserById(authenticatedUserId);
                if (user != null) {
                    List<Trip> trips = tripService.getTripsByCreatorId(user);
                    trips.sort(Comparator.comparing(Trip::getStartDate));
                    model.addAttribute("user", user);
                    model.addAttribute("trips", trips);
                    model.addAttribute("loggedInUser", loggedInUser);

                    return "main";
                }
            }
        }
        return "redirect:/index";
    }


    @GetMapping("/explore")
    public String explorePage(Model model, @CookieValue(value = "authToken", defaultValue = "") String authToken) {
        if (!authToken.isEmpty()) {
            Long authenticatedUserId = jwtTokenUtil.extractUserId(authToken);

            if (authenticatedUserId != null && jwtTokenUtil.validateToken(authToken, authenticatedUserId)) {
                User loggedInUser = userService.getUserById(authenticatedUserId);
                User user = userService.getUserById(authenticatedUserId);
                if (user != null) {
                    List<Trip> allTrips = tripService.getAllTrips();
                    allTrips.sort(Comparator.comparing(Trip::getStartDate));
                    model.addAttribute("user", user);
                    model.addAttribute("allTrips", allTrips);
                    model.addAttribute("loggedInUser", loggedInUser);
                    return "explore";
                } else {
                    return "redirect:/index";
                }
            }
        }
        return "redirect:/index";
    }

    @GetMapping("/tripcard")
    public String tripCardPage(@RequestParam("tripId") Long tripId, @RequestParam("userId") Long userId, Model model, @CookieValue(value = "authToken", defaultValue = "") String authToken) {
        if (!authToken.isEmpty()) {
            Long authenticatedUserId = jwtTokenUtil.extractUserId(authToken);
            if (authenticatedUserId != null && jwtTokenUtil.validateToken(authToken, authenticatedUserId)) {
                User loggedInUser = userService.getUserById(authenticatedUserId);
                Trip trip = tripService.getTripById(tripId);
                User user = userService.getUserById(userId);

                if (trip != null && user != null) {
                    List<Comment> comments = commentService.getCommentsForTrip(tripId);
                    List<Image> images = imageService.getImagesByTrip(trip);
                    model.addAttribute("images", images);
                    model.addAttribute("trip", trip);
                    model.addAttribute("user", user);
                    model.addAttribute("loggedInUser", loggedInUser);
                    model.addAttribute("comments", comments);
                    return "tripcard";
                }
            }
        }
        return "redirect:/index";
    }


    @GetMapping("/profile/{userId}")
    public String userProfile(@PathVariable Long userId, Model model, @CookieValue(value = "authToken", defaultValue = "") String authToken) {
        if (!authToken.isEmpty()) {
            Long authenticatedUserId = jwtTokenUtil.extractUserId(authToken);
            if (authenticatedUserId != null && jwtTokenUtil.validateToken(authToken, authenticatedUserId)) {
                User loggedInUser = userService.getUserById(authenticatedUserId);
                User profileUser = userService.getUserById(userId);

                if (profileUser == null) {
                    throw new IllegalStateException("User not found");
                }

                model.addAttribute("loggedInUser", loggedInUser);
                model.addAttribute("user", profileUser);
                model.addAttribute("trips", tripService.getTripsByCreatorId(profileUser));

                return "profile";
            }
        }
        return "redirect:/index";
    }

    @GetMapping("/offers")
    public String showOffersPage(Model model, @CookieValue(value = "authToken", defaultValue = "") String authToken) {
        if (!authToken.isEmpty()) {
            Long authenticatedUserId = jwtTokenUtil.extractUserId(authToken);
            if (authenticatedUserId != null && jwtTokenUtil.validateToken(authToken, authenticatedUserId)) {
                User loggedInUser = userService.getUserById(authenticatedUserId);
                User profileUser = userService.getUserById(authenticatedUserId);

                if (profileUser == null) {
                    throw new IllegalStateException("User not found");
                }
                model.addAttribute("loggedInUser", loggedInUser);
                model.addAttribute("user", profileUser);

                return "offers";
            }
        }

        return "redirect:/index";
    }
}

