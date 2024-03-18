package com.team10.cen.service;

import com.team10.cen.domain.Scholarship;
import com.team10.cen.repository.ScholarshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
}