package com.example.TravelMore.UserAccount;
import com.example.TravelMore.model.LoginRequest;
import com.example.TravelMore.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping(path = "api/travelMore")
public class UserRestController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserRestController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            User newUser = userService.createUser(user);
            return ResponseEntity.ok().body(newUser);
        } catch (IllegalArgumentException  | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(path = "/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") Long userId, @RequestHeader(name = "Authorization") String token) {
        if (jwtTokenUtil.validateToken(token, userId)) {
            Long authenticatedUserId = jwtTokenUtil.extractUserId(token);
            if (userId.equals(authenticatedUserId)) {
                User user = userService.getUserById(userId);
                if (user != null) {
                    return ResponseEntity.ok().body(user);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping(path = "/users/{userId}")
    public ResponseEntity<String> deleteAccount(@PathVariable("userId") Long userId, @RequestHeader(name = "Authorization") String token) {
        if (jwtTokenUtil.validateToken(token, userId)) {
            Long authenticatedUserId = jwtTokenUtil.extractUserId(token);
            if (userId.equals(authenticatedUserId)) {
                userService.deleteUser(userId);
                return ResponseEntity.ok().body("User deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @PutMapping(path = "/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long userId, @RequestBody User userUpdates, @RequestHeader(name = "Authorization") String token) {
        if (jwtTokenUtil.validateToken(token, userId)) {
            Long authenticatedUserId = jwtTokenUtil.extractUserId(token);
            if (userId.equals(authenticatedUserId)) {
                User updatedUser = userService.updateUser(userId, userUpdates);
                if (updatedUser != null) {
                    return ResponseEntity.ok().body(updatedUser);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping(path = "/users")
    public List<User> getAccounts() {
        return userService.getUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.authenticateUser(loginRequest);
        if (user != null) {
            String token = jwtTokenUtil.generateToken(user.getId());
            return ResponseEntity.ok().body(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username/password");
        }
    }
}