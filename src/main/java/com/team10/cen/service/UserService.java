package com.team10.cen.service;

import com.team10.cen.domain.Save;
import com.team10.cen.domain.Scholarship;
import com.team10.cen.domain.User;
import com.team10.cen.repository.SaveRepository;
import com.team10.cen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public void scrapScholarshipById(String userId, Long scholarshipId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the scholarship is already saved by the user
        boolean alreadySaved = user.getSaves().stream()
                .anyMatch(save -> save.getScholarship().getId().equals(scholarshipId));

        if (alreadySaved) {
            throw new RuntimeException("You have already saved this scholarship.");
        }

        Scholarship scholarship = scholarshipService.findById(scholarshipId);
        if (scholarship != null) {
            Save save = new Save();
            save.setUser(user);
            save.setScholarship(scholarship);
            user.getSaves().add(save);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Scholarship not found with id: " + scholarshipId);
        }
    }

    @Autowired
    private SaveRepository saveRepository;

    public boolean cancelScrapScholarshipById(String userId, Long scholarshipId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        Optional<Save> save = saveRepository.findByUserAndScholarshipId(user, scholarshipId);
        if (!save.isPresent()) {
            throw new RuntimeException("Scrap record not found for user with ID: " + userId + " and scholarship ID: " + scholarshipId);
        }

        saveRepository.delete(save.get()); // 스크랩 내역 삭제
        return true; // 스크랩 취소 성공
    }
}