package com.example.TravelMore.UserAccount;

import com.example.TravelMore.model.LoginRequest;
import com.example.TravelMore.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(path = "/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            User newUser = userService.createUser(user);
            model.addAttribute("user", newUser);
            return "index"; // Change the redirect page if necessary
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "registrationFailure";
        }
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "register";
    }

    @GetMapping(path = "/{id}")
    public String getUserById(@PathVariable("id") Long id, @RequestHeader(name = "Authorization") String token, Model model) {
        if (jwtTokenUtil.validateToken(token, id)) {
            Long authenticatedUserId = jwtTokenUtil.extractUserId(token);
            if (id.equals(authenticatedUserId)) {
                User user = userService.getUserById(id);
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

    @DeleteMapping(path = "/{id}")
    public String deleteAccount(@PathVariable("id") Long id, @RequestHeader(name = "Authorization") String token, Model model) {
        if (jwtTokenUtil.validateToken(token, id)) {
            Long authenticatedUserId = jwtTokenUtil.extractUserId(token);
            if (id.equals(authenticatedUserId)) {
                userService.deleteUser(id);
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

    @PutMapping(path = "/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("userUpdates") User userUpdates, @RequestHeader(name = "Authorization") String token, Model model) {
        if (jwtTokenUtil.validateToken(token, id)) {
            Long authenticatedUserId = jwtTokenUtil.extractUserId(token);
            if (id.equals(authenticatedUserId)) {
                User updatedUser = userService.updateUser(id, userUpdates);
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

    @GetMapping(path = "/all")
    public String getAccounts(Model model) {
        List<User> users = userService.getUsers();
        model.addAttribute("users", users);
        return "userList";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
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
}
