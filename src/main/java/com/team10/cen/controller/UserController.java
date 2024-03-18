package com.team10.cen.controller;

import com.team10.cen.domain.User;
import com.team10.cen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
}