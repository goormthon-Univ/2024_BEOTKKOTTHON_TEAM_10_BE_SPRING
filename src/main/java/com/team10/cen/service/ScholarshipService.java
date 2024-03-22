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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    public Scholarship getScholarshipById(long id) {
        Optional<Scholarship> scholarshipOptional = scholarshipRepository.findById(id);
        return scholarshipOptional.orElseThrow(() -> new IllegalArgumentException("Scholarship with ID " + id + " not found"));
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

                // getDDay()를 호출하여 D-DAY를 사용하는 예제
                Long dDay = scholarship.getDDay();
                // 이제 dDay를 사용할 수 있습니다.
            }
        }
        return recommendedScholarships;
    }

    private Long calculateDDay(String endDate) {
        if (endDate != null) {
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
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
}