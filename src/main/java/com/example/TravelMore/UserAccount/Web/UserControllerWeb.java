package com.example.TravelMore.UserAccount.Web;

import com.example.TravelMore.UserAccount.User;
import com.example.TravelMore.UserAccount.UserService;
import com.example.TravelMore.model.LoginRequest;
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
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserControllerWeb(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(path = "/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            User newUser = userService.createUser(user);
            model.addAttribute("user", newUser);
            return "login";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "registrationFailure";
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
            String token = jwtTokenUtil.generateToken(user.getUserId());
            model.addAttribute("token", token);
            return "userAccount";
        } else {
            model.addAttribute("error", "Invalid username/password");
            return "signUp";
        }
    }
}