package com.team10.cen.service;

import com.team10.cen.domain.Scholarship;
import com.team10.cen.domain.User;
import com.team10.cen.repository.ScholarshipRepository;
import com.team10.cen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScholarshipService {
    private final ScholarshipRepository scholarshipRepository;

    @Autowired
    public ScholarshipService(ScholarshipRepository scholarshipRepository) {
        this.scholarshipRepository = scholarshipRepository;
    }
    public List<Scholarship> getAllScholarships() {
        return scholarshipRepository.findAll();
    }

    public Scholarship getScholarshipById(long id) {
        Optional<Scholarship> scholarshipOptional = scholarshipRepository.findById(id);
        return scholarshipOptional.orElseThrow(() -> new IllegalArgumentException("Scholarship with ID " + id + " not found"));
    }

    @Autowired
    private UserRepository userRepository;

    public List<Scholarship> getRecommendedScholarships(String userid) {
        User user = userRepository.findByUserId(userid);
        List<Scholarship> allScholarships = scholarshipRepository.findAll();
        List<Scholarship> recommendedScholarships = new ArrayList<>();

        for (Scholarship scholarship : allScholarships) {
            if (isScholarshipEligible(user, scholarship)) {
                recommendedScholarships.add(scholarship);
            }
        }
        return recommendedScholarships;
    }

    private boolean isScholarshipEligible(User user, Scholarship scholarship) {
        // Implement eligibility logic here
        return user.getRanking().equals(scholarship.getSupportRanking()) &&
                user.getGrade().equals(scholarship.getSupportGrade()) &&
                user.getRegionCityProvince().equals(scholarship.getSupportCityProvince()) &&
                user.getRegionCityCountyDistrict().equals(scholarship.getSupportCityCountyDistrict()) &&
                user.getMajor().equals(scholarship.getSupportMajor());
    }
}