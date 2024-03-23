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
        recommendedScholarships.sort((scholarship1, scholarship2) -> {
            long dDay1 = scholarship1.getDDay();
            long dDay2 = scholarship2.getDDay();

            if (dDay1 >= 0 && dDay2 >= 0) {
                return Long.compare(dDay1, dDay2);
            } else if (dDay1 < 0 && dDay2 < 0) {
                // 둘 다 음수인 경우, 절대값 비교 후 내림차순 정렬
                return -Long.compare(Math.abs(dDay1), Math.abs(dDay2));
            } else {
                // 한 쪽은 양수, 다른 한 쪽은 음수인 경우
                return dDay1 >= 0 ? -1 : 1;
            }
        });

        return recommendedScholarships;
    }

    public List<Scholarship> getRecommendedScholarshipsUpdate(String userId) {
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

// Sort in descending order based on id
        recommendedScholarships.sort(Comparator.comparingLong(Scholarship::getId).reversed());

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
        // 변경된 부분 시작
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            return false; // 해당 유저를 찾을 수 없음
        }

        Optional<Save> save = saveRepository.findByUserAndScholarshipId(user, scholarshipId);
        if (!save.isPresent()) {
            return false; // 해당 유저의 지원 내역이 없음
        }

        Save savedRecord = save.get();
        savedRecord.setStatus(status);
        saveRepository.save(savedRecord);
        return true; // 상태 업데이트 성공
        // 변경된 부분 끝
    }
}