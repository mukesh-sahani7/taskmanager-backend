package com.example.taskmanager.controller;

import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. SIGNUP / REGISTER ENDPOINT
   @PostMapping("/signup")
public ResponseEntity<?> registerUser(@RequestBody User user) {
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        // Plain string ke bajaye ek proper JSON object (Map) banakar bhej rahe hain
        java.util.Map<String, String> errorResponse = new java.util.HashMap<>();
        errorResponse.put("error", "Error: Email is already taken!");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Password ko encrypt karke save kar rahe hain
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    User savedUser = userRepository.save(user);
    return ResponseEntity.ok(savedUser);
}

    // 2. LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Password check kar rahe hain
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

                // Login successful hone par frontend ke liye token aur user info bhej rahe hain
                Map<String, Object> response = new HashMap<>();
                response.put("token", "dummy-jwt-token-for-user-" + user.getId()); // Real JWT flow ki tarah token string
                response.put("userId", user.getId());
                response.put("name", user.getName());
                response.put("profilePic", user.getProfilePic());

                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.status(401).body("Error: Invalid Email or Password!");
    }

    @PutMapping("/update-profile-pic/{userId}")
    public ResponseEntity<?> updateProfilePic(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setProfilePic(request.get("profilePic")); // Base64 string ko save kar rahe hain
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(404).body("User not found");
    }
}
