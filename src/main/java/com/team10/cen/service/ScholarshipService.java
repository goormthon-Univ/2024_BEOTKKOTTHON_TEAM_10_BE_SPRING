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

import java.math.BigDecimal;
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
        return scholarshipRepository.findAllOrderByDDayAsc();
    }

    public List<Scholarship> getAllScholarshipsSortedByCreatedAt() {
        return scholarshipRepository.findAllOrderByCreatedAtDesc();
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
        // Check if user's ranking, grade, city/province, and major are all included in supported attributes of the scholarship
        boolean isCityProvinceSupported = scholarship.getSupportCityProvince() != null && scholarship.getSupportCityProvince().contains(user.getRegionCityProvince());
        boolean isCityCountyDistrictSupported = scholarship.getSupportCityProvince() != null && scholarship.getSupportCityProvince().contains(user.getRegionCityCountryDistrict());

        // Handle cases where supported attributes might be null
        String supportRanking = scholarship.getSupportRanking();
        String supportGrade = scholarship.getSupportGrade();
        String supportMajor = scholarship.getSupportMajor();

        return supportRanking != null && supportRanking.contains(user.getRanking()) &&
                supportGrade != null && supportGrade.contains(user.getGrade()) &&
                (isCityProvinceSupported || isCityCountyDistrictSupported) &&
                supportMajor != null && supportMajor.contains(user.getMajor());
    }

    // Method to calculate the total amount of recommended scholarships
    public BigDecimal calculateTotalAmount(List<Scholarship> scholarships) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Scholarship scholarship : scholarships) {
            totalAmount = totalAmount.add(new BigDecimal(scholarship.getAmount()));
        }
        return totalAmount;
    }
}