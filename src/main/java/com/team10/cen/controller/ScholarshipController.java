package com.team10.cen.controller;

import com.team10.cen.domain.Scholarship;
import com.team10.cen.service.ScholarshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}