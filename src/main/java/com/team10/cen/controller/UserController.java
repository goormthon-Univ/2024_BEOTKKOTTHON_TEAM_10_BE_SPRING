package com.team10.cen.controller;

import com.team10.cen.domain.Scholarship;
import com.team10.cen.domain.User;
import com.team10.cen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/each")
    public ResponseEntity<User> getUserById(@RequestHeader("userid") String userid) {
        User user = userService.getUserById(userid);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testEndpoint(@RequestHeader("userid") String userId) {
        Map<String, String> response = new HashMap<>();
        response.put("userId", userId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/scholarship/each/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> scrapScholarshipById(@RequestHeader("userid") String userId, @RequestBody Map<String, Long> requestBody) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long scholarshipId = requestBody.get("scholarshipId");
            userService.scrapScholarshipById(userId, scholarshipId);
            response.put("status", "success");
            response.put("message", "Scholarship successfully scraped.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    // 취소를 위한 새로운 메서드 추가
    @PostMapping("/scholarship/each/cancel")
    public ResponseEntity<String> cancelScrapScholarshipById(@RequestHeader("userid") String userId, @RequestBody Map<String, Long> requestBody) {
        try {
            Long scholarshipId = requestBody.get("scholarshipId");
            boolean cancellationStatus = userService.cancelScrapScholarshipById(userId, scholarshipId);
            if (cancellationStatus) {
                return ResponseEntity.status(HttpStatus.OK).body("Scholarship scrap successfully cancelled.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Scholarship scrap not found for the given user and scholarship ID.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while cancelling scholarship scrap: " + e.getMessage());
        }
    }
}