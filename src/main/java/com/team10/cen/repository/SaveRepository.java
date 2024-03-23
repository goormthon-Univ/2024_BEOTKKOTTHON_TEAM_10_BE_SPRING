package com.team10.cen.repository;

import com.team10.cen.domain.Save;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaveRepository extends JpaRepository<Save, Long> {

    Save findByUserUserIdAndScholarshipId(String userUserId, Long scholarshipId);
}