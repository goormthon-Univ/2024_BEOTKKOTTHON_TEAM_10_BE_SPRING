package com.team10.cen.service;

import com.team10.cen.domain.Save;
import com.team10.cen.domain.Scholarship;
import com.team10.cen.domain.User;
import com.team10.cen.repository.SaveRepository;
import com.team10.cen.repository.ScholarshipRepository;
import com.team10.cen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ScholarshipService {
    private final ScholarshipRepository scholarshipRepository;

    @Autowired
    public ScholarshipService(ScholarshipRepository scholarshipRepository) {
        this.scholarshipRepository = scholarshipRepository;
    }

    public Scholarship getScholarshipById(long id) {
        Optional<Scholarship> scholarshipOptional = scholarshipRepository.findById(id);
        Scholarship scholarship = scholarshipOptional.orElseThrow(() -> new IllegalArgumentException("Scholarship with ID " + id + " not found"));

        // D-DAY를 계산하여 Scholarship 객체에 할당
        scholarship.setDDay(calculateDDay(scholarship.getEndDate()));

        return scholarship;
    }

    @Autowired
    private UserRepository userRepository;

    public List<Scholarship> getRecommendedScholarships(String userId) {
        User user = userRepository.findByUserId(userId);
        List<Scholarship> allScholarships = scholarshipRepository.findAll();
        List<Scholarship> recommendedScholarships = new ArrayList<>();

        for (Scholarship scholarship : allScholarships) {
            if (isScholarshipEligible(user, scholarship)) {
                // D-DAY를 계산하여 Scholarship 객체에 할당
                scholarship.setDDay(calculateDDay(scholarship.getEndDate()));
                recommendedScholarships.add(scholarship);
            }
        }

        // D-DAY를 기준으로 오름차순으로 정렬
        recommendedScholarships.sort(Comparator.comparingLong(Scholarship::getDDay));

        return recommendedScholarships;
    }

    private Long calculateDDay(String endDate) {
        if (endDate != null) {
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate now = LocalDate.now();
            return now.until(end, ChronoUnit.DAYS);
        } else {
            return null;
        }
    }

    private boolean isScholarshipEligible(User user, Scholarship scholarship) {
        // Check if user's ranking, grade, city/province, and major are all included in supported attributes of the scholarship
        boolean isCityProvinceSupported = scholarship.getSupportCityProvince() != null && !scholarship.getSupportCityProvince().equals("해당없음") && scholarship.getSupportCityProvince().contains(user.getRegionCityProvince());
        boolean isCityCountyDistrictSupported = scholarship.getSupportCityProvince() != null && !scholarship.getSupportCityProvince().equals("해당없음") && scholarship.getSupportCityProvince().contains(user.getRegionCityCountryDistrict());

        // Handle cases where supported attributes might be null
        String supportRanking = scholarship.getSupportRanking();
        String supportGrade = scholarship.getSupportGrade();
        String supportMajor = scholarship.getSupportMajor();

        // If supportCityProvince is "Not Applicable", consider it supported
        if ("해당없음".equals(scholarship.getSupportCityProvince())) {
            isCityProvinceSupported = true;
            isCityCountyDistrictSupported = true;
        }

        return supportRanking != null && supportRanking.contains(user.getRanking()) &&
                supportGrade != null && supportGrade.contains(user.getGrade()) &&
                (isCityProvinceSupported || isCityCountyDistrictSupported) &&
                supportMajor != null && supportMajor.contains(user.getMajor());
    }

    public BigDecimal calculateTotalAmount(List<Scholarship> scholarships) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Scholarship scholarship : scholarships) {
            String amountString = scholarship.getAmount();
            if (amountString.contains(",")) {
                // If the amount string contains commas, remove them before parsing
                amountString = amountString.replace(",", "");
            }
            try {
                BigDecimal amount = new BigDecimal(amountString);
                totalAmount = totalAmount.add(amount);
            } catch (NumberFormatException e) {
                // Handle the case where the amount string cannot be parsed as a BigDecimal
                System.err.println("Invalid amount format: " + amountString);
                // You can log the error or handle it in another appropriate way
            }
        }
        return totalAmount;
    }

    public Scholarship findById(Long id) {
        return scholarshipRepository.findById(id).orElse(null);
    }

    @Autowired
    private SaveRepository saveRepository;

    @Transactional
    public boolean updateScholarshipStatus(String userId, Long scholarshipId, Save.Status status) {
        Save save = saveRepository.findByUserUserIdAndScholarshipId(userId, scholarshipId);
        if (save == null) {
            return false; // Save record not found
        }

        save.setStatus(status);
        saveRepository.save(save);

        return true; // Status updated successfully
    }
}