package com.team10.cen.controller;

import com.team10.cen.domain.Scholarship;
import com.team10.cen.service.ScholarshipService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/scholarship/all")
    public List<Scholarship> getAllScholarships() {
        return scholarshipService.getAllScholarships();
    }

    @GetMapping("/scholarship/all/new")
    public List<Scholarship> getAllScholarshipsSortedByCreatedAt() {
        return scholarshipService.getAllScholarshipsSortedByCreatedAt();
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
    public ResponseEntity<List<Scholarship>> getRecommendedScholarships(@RequestHeader("userid") String userId) {
        List<Scholarship> recommendedScholarships = scholarshipService.getRecommendedScholarships(userId);
        return ResponseEntity.ok(recommendedScholarships);
    }

    @GetMapping("/scholarship/user/amount")
    public ResponseEntity<BigDecimal> getTotalAmountOfRecommendedScholarships(@RequestHeader("userid") String userId) {
        List<Scholarship> recommendedScholarships = scholarshipService.getRecommendedScholarships(userId);
        BigDecimal totalAmount = scholarshipService.calculateTotalAmount(recommendedScholarships);
        return ResponseEntity.ok(totalAmount);
    }
}