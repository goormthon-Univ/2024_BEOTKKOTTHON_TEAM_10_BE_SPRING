package com.team10.cen.service;

import com.team10.cen.domain.Scholarship;
import com.team10.cen.repository.ScholarshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

}