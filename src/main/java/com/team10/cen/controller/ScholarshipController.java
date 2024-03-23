package com.team10.cen.controller;

import com.team10.cen.domain.Save;
import com.team10.cen.domain.Scholarship;
import com.team10.cen.service.ScholarshipService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class ScholarshipController {

    private final ScholarshipService scholarshipService;

    @Autowired
    public ScholarshipController(ScholarshipService scholarshipService) {
        this.scholarshipService = scholarshipService;
    }

    @GetMapping("/scholarship/each")
    public ResponseEntity<Scholarship> getScholarshipById(@RequestHeader("scholarshipId") long id) {
        Scholarship scholarship = scholarshipService.getScholarshipById(id);
        if (scholarship == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(scholarship);
    }

    @GetMapping("/scholarship/user")
    public ResponseEntity<?> getRecommendedScholarships(@RequestHeader("userid") String userId) {
        List<Scholarship> recommendedScholarships = scholarshipService.getRecommendedScholarships(userId);

        if (recommendedScholarships.isEmpty()) {
            // 에러 메시지를 JSON 형식으로 반환
            ErrorMessage error = new ErrorMessage("No recommended scholarships found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } else {
            return ResponseEntity.ok(recommendedScholarships);
        }
    }

    @Getter @Setter
    // 에러 메시지 클래스
    private static class ErrorMessage {
        private String message;

        public ErrorMessage(String message) {
            this.message = message;
        }
    }

    @GetMapping("/scholarship/user/new")
    public ResponseEntity<?> getRecommendedScholarshipsUpdate(@RequestHeader("userid") String userId) {
        List<Scholarship> recommendedScholarships = scholarshipService.getRecommendedScholarshipsUpdate(userId);

        if (recommendedScholarships.isEmpty()) {
            // 에러 메시지를 JSON 형식으로 반환
            ErrorMessage error = new ErrorMessage("No recommended scholarships found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } else {
            return ResponseEntity.ok(recommendedScholarships);
        }
    }

    @GetMapping("/scholarship/user/amount")
    public ResponseEntity<Object> getTotalAmountOfRecommendedScholarships(@RequestHeader("userid") String userId) {
        List<Scholarship> recommendedScholarships = scholarshipService.getRecommendedScholarships(userId);
        BigDecimal totalAmount = scholarshipService.calculateTotalAmount(recommendedScholarships);

        // Create a JSON object to hold the total amount
        TotalAmountResponse response = new TotalAmountResponse(totalAmount);

        return ResponseEntity.ok(response);
    }

    @Getter @Setter
    static class TotalAmountResponse {
        private BigDecimal totalAmount;

        public TotalAmountResponse(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }
    }

    @PostMapping("/scholarship/each/status")
    public ResponseEntity<String> updateScholarshipStatus(
            @RequestBody UpdateStatusRequest request,
            @RequestHeader("USERID") String userId) {

        boolean success = scholarshipService.updateScholarshipStatus(userId, request.getScholarshipId(), request.getStatus());

        if (success) {
            return ResponseEntity.ok("Status updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update status");
        }
    }

    @Getter @Setter
    public static class UpdateStatusRequest {
        private Long scholarshipId;
        private Save.Status status;
    }
}