package com.team10.cen.repository;

import com.team10.cen.domain.Save;
import com.team10.cen.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaveRepository extends JpaRepository<Save, Long> {

    Optional<Save> findByUserAndScholarshipId(User user, Long scholarshipId);
}