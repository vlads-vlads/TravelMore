package com.example.TravelMore.web;
import com.example.TravelMore.Comment.CommentService;
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
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.List;

@Controller
public class MainController {

    private final UserService userService;
    private final TripService tripService;
    private final CommentService commentService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public MainController(UserService userService, TripService tripService, CommentService commentService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.tripService = tripService;
        this.commentService = commentService;
        this.jwtTokenUtil = jwtTokenUtil;
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

        List<Trip> trips = tripService.getTripsByCreatorId(user);

        model.addAttribute("user", user);
        model.addAttribute("trips", trips);

        return "main";
    }

    @GetMapping("/main")
    public String mainPage(Model model, @CookieValue(value = "authToken", defaultValue = "") String authToken) {
        if (!authToken.isEmpty()) {

            Long userId = jwtTokenUtil.extractUserId(authToken);
            User user = userService.getUserById(userId);

            if (user != null) {

                List<Trip> trips = tripService.getTripsByCreatorId(user);

                model.addAttribute("user", user);
                model.addAttribute("trips", trips);

                return "main";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/explore")
    public String explorePage(Model model, @CookieValue(value = "authToken", defaultValue = "") String authToken) {
        if (!authToken.isEmpty()) {
            Long userId = jwtTokenUtil.extractUserId(authToken);
            if (userId != null && jwtTokenUtil.validateToken(authToken, userId)) {
                User user = userService.getUserById(userId);
                if (user != null) {
                    List<Trip> allTrips = tripService.getAllTrips();
                    model.addAttribute("user", user);
                    model.addAttribute("allTrips", allTrips);
                    return "explore";
                } else {
                    return "redirect:/login";
                }
            }
        }
        return "redirect:/login";
    }

}
