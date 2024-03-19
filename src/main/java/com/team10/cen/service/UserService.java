package com.team10.cen.service;

import com.team10.cen.domain.Scholarship;
import com.team10.cen.domain.User;
import com.team10.cen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String userid) {
        return userRepository.findById(userid).orElse(null);
    }

    @Autowired
    private ScholarshipService scholarshipService;

    public void scrapScholarshipById(String userId, Long scholarshipId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Scholarship scholarship = scholarshipService.findById(scholarshipId);
        if (scholarship != null) {
            user.getScrappedScholarships().add(scholarship);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Scholarship not found with id: " + scholarshipId);
        }
    }
}