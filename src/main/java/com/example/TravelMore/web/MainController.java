package com.example.TravelMore.web;
import com.example.TravelMore.Comment.Comment;
import com.example.TravelMore.Comment.CommentService;
import com.example.TravelMore.Image.Image;
import com.example.TravelMore.Image.ImageService;
import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserService;
import com.example.TravelMore.joinRequest.JoinRequest;
import com.example.TravelMore.joinRequest.JoinRequestService;
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

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private final UserService userService;
    private final TripService tripService;
    private final CommentService commentService;
    private final JwtTokenUtil jwtTokenUtil;

    private final ImageService imageService;

    private final JoinRequestService joinRequestService;

    @Autowired
    public MainController(UserService userService, TripService tripService, CommentService commentService, JwtTokenUtil jwtTokenUtil, ImageService imageService, JoinRequestService joinRequestService) {
        this.userService = userService;
        this.tripService = tripService;
        this.commentService = commentService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.imageService = imageService;

        this.joinRequestService = joinRequestService;
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

        List<Trip> allTrips = tripService.getTripsCreatedByUser(user.getId());

        List<Trip> posts = allTrips.stream().filter(Trip::isAPost).collect(Collectors.toList());
        List<Trip> trips = allTrips.stream().filter(trip -> !trip.isAPost()).collect(Collectors.toList());

        posts.sort(Comparator.comparing(Trip::getStartDate));
        trips.sort(Comparator.comparing(Trip::getStartDate));
        model.addAttribute("user", user);
        model.addAttribute("incompleteTrips", trips);
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("completeTrips", posts);

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

                    List<Trip> allTrips = tripService.getTripsCreatedByUser(user.getId());

                    List<Trip> posts = allTrips.stream().filter(Trip::isAPost).collect(Collectors.toList());
                    List<Trip> trips = allTrips.stream().filter(trip -> !trip.isAPost()).collect(Collectors.toList());

                    posts.sort(Comparator.comparing(Trip::getStartDate));
                    trips.sort(Comparator.comparing(Trip::getStartDate));
                    model.addAttribute("user", user);
                    model.addAttribute("incompleteTrips", trips);
                    model.addAttribute("loggedInUser", loggedInUser);
                    model.addAttribute("completeTrips", posts);

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
                User user = userService.getUserById(authenticatedUserId);

                List<Trip> allTrips = tripService.getAllTrips();

                List<Trip> posts = allTrips.stream().filter(Trip::isAPost).collect(Collectors.toList());
                List<Trip> trips = allTrips.stream().filter(trip -> !trip.isAPost()).collect(Collectors.toList());

                posts.sort(Comparator.comparing(Trip::getStartDate));
                trips.sort(Comparator.comparing(Trip::getStartDate));

                model.addAttribute("user", user);
                model.addAttribute("completeTrips", trips);
                model.addAttribute("incompleteTrips", posts);
                model.addAttribute("loggedInUser", user);

                return "explore";
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
                Set<User> participants = trip.getParticipants();


                if (trip != null && user != null) {
                    List<Comment> comments = commentService.getCommentsForTrip(tripId);
                    List<Image> images = imageService.getImagesByTrip(trip);
                    model.addAttribute("images", images);
                    model.addAttribute("trip", trip);
                    model.addAttribute("user", user);
                    model.addAttribute("loggedInUser", loggedInUser);
                    model.addAttribute("comments", comments);
                    model.addAttribute("participants", participants);
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
                List<Trip> trips = tripService.getTripsCreatedByUser(profileUser.getId());

                List<Trip> completeTrips = trips.stream()
                        .filter(trip -> trip.getStartDate() != null && trip.getEndDate() != null)
                        .collect(Collectors.toList());

                List<Trip> incompleteTrips = trips.stream()
                        .filter(trip -> trip.getStartDate() == null || trip.getEndDate() == null)
                        .collect(Collectors.toList());

                List<String> avatars = Arrays.asList(
                        "/images/cat.png",
                        "/images/beardman.png",
                        "/images/longhair.png",
                        "/images/man.png",
                        "/images/mustache.png",
                        "/images/panda.png",
                        "/images/woman.png"
                );

                completeTrips.sort(Comparator.comparing(Trip::getStartDate));
                model.addAttribute("loggedInUser", loggedInUser);
                model.addAttribute("user", profileUser);
                model.addAttribute("trips", tripService.getTripsCreatedByUser(profileUser.getId()));
                model.addAttribute("completeTrips", completeTrips);
                model.addAttribute("incompleteTrips", incompleteTrips);
                model.addAttribute("avatars", avatars);

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
                User profileUser = userService.getUserById(loggedInUser.getId());
                if (profileUser == null) {
                    throw new IllegalStateException("User not found");
                }

                List<JoinRequest> allRequests = joinRequestService.getAllJoinRequests();

                List<JoinRequest> filteredRequests = new ArrayList<>();
                for (JoinRequest request : allRequests) {
                    if (request.getReceiver().getId().equals(loggedInUser.getId())) {
                        filteredRequests.add(request);
                    }
                }

                model.addAttribute("requests", filteredRequests);
                model.addAttribute("loggedInUser", loggedInUser);
                model.addAttribute("user", profileUser);

                return "offers";
            }
        }

        return "redirect:/index";
    }


}

