package com.example.TravelMore.UserAccount.Web;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserService;
import com.example.TravelMore.joinRequest.JoinRequestService;
import com.example.TravelMore.model.LoginRequest;
import com.example.TravelMore.trip.Trip;
import com.example.TravelMore.trip.TripService;
import com.example.TravelMore.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "travelMore")
public class UserControllerWeb {

    private final UserService userService;

    private final TripService tripService;
    private final JwtTokenUtil jwtTokenUtil;

    private final JoinRequestService joinRequestService;

    @Autowired
    public UserControllerWeb(UserService userService, TripService tripService, JwtTokenUtil jwtTokenUtil, JoinRequestService joinRequestService) {
        this.userService = userService;
        this.tripService = tripService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.joinRequestService = joinRequestService;
    }

    @PostMapping(path = "/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            User newUser = userService.createUser(user);
            model.addAttribute("user", newUser);
            return "index";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage";
        }
    }

    @GetMapping(path = "/users/{userId}")
    public String getUserById(@PathVariable("userId") Long userId, @RequestHeader(name = "Authorization") String token, Model model) {
        if (jwtTokenUtil.validateToken(token, userId)) {
            Long authenticatedUserId = jwtTokenUtil.extractUserId(token);
            if (userId.equals(authenticatedUserId)) {
                User user = userService.getUserById(userId);
                if (user != null) {
                    model.addAttribute("user", user);
                    return "userAccount";
                } else {
                    return "login";
                }
            } else {
                return "login";
            }
        } else {
            return "login";
        }
    }

    @DeleteMapping(path = "/users/{userId}")
    public String deleteAccount(@PathVariable("userId") Long userId, @RequestHeader(name = "Authorization") String token, Model model) {
        if (jwtTokenUtil.validateToken(token, userId)) {
            Long authenticatedUserId = jwtTokenUtil.extractUserId(token);
            if (userId.equals(authenticatedUserId)) {
                userService.deleteUser(userId);
                model.addAttribute("message", "User deleted successfully");
                return "userAccount";
            } else {
                model.addAttribute("error", "Forbidden");
                return "login";
            }
        } else {
            model.addAttribute("error", "Unauthorized");
            return "login";
        }
    }

    @PutMapping(path = "/users/{userId}")
    public String updateUser(@PathVariable("userId") Long userId, @ModelAttribute("userUpdates") User userUpdates, @RequestHeader(name = "Authorization") String token, Model model) {
        if (jwtTokenUtil.validateToken(token, userId)) {
            Long authenticatedUserId = jwtTokenUtil.extractUserId(token);
            if (userId.equals(authenticatedUserId)) {
                User updatedUser = userService.updateUser(userId, userUpdates);
                if (updatedUser != null) {
                    model.addAttribute("user", updatedUser);
                    return "userAccount";
                } else {
                    return "login";
                }
            } else {
                model.addAttribute("error", "Forbidden");
                return "login";
            }
        } else {
            model.addAttribute("error", "Unauthorized");
            return "login";
        }
    }

    @GetMapping(path = "/users")
    public String getAccounts(Model model) {
        List<User> users = userService.getUsers();
        model.addAttribute("users", users);
        return "userList";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginRequest") LoginRequest loginRequest, Model model) {
        User user = userService.authenticateUser(loginRequest);
        if (user != null) {
            String token = jwtTokenUtil.generateToken(user.getId());
            model.addAttribute("token", token);
            return "userAccount";
        } else {
            model.addAttribute("error", "Invalid username/password");
            return "signUp";
        }
    }

    @GetMapping("/{userId}/participated-trips")
    public String getParticipatedTrips(@PathVariable Long userId, Model model) {
        List<Trip> participatedTrips = tripService.getParticipatedTrips(userId);
        model.addAttribute("participatedTrips", participatedTrips);
        return "participatedTrips";
    }

//    @GetMapping("/{userId}/join-requests")
//    public String getJoinRequestsForUser(@PathVariable Long userId, Model model) {
//        List<JoinRequest> joinRequests = joinRequestService.getJoinRequestsForUser(userId);
//        model.addAttribute("joinRequests", joinRequests);
//        return "joinRequests";
//    }

    @GetMapping("/{userId}/created-trips")
    public String getCreatedTrips(@PathVariable Long userId, Model model) {
        List<Trip> createdTrips = tripService.getTripsCreatedByUser(userId);
        model.addAttribute("createdTrips", createdTrips);
        return "createdTrips";
    }

    @PostMapping("/saveAvatar")
    public String saveAvatar(@RequestParam("userId") Long userId, @RequestParam("avatarFileName") String avatarFileName) {
        try {
            String trimmedFileName = avatarFileName.trim();
            userService.updateUserAvatar(userId, trimmedFileName);
            return "redirect:/profile/" + userId;
        } catch (IllegalArgumentException | IllegalStateException e) {
            return "errorPage";
        }
    }

}