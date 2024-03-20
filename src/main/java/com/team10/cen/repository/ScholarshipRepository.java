package com.team10.cen.repository;

import com.team10.cen.domain.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {
    @Query("SELECT s FROM Scholarship s ORDER BY s.dDay ASC")
    List<Scholarship> findAllOrderByDDayAsc();

    // Custom query to fetch all scholarships sorted by creation time in descending order
    @Query("SELECT s FROM Scholarship s ORDER BY s.createdAt DESC")
    List<Scholarship> findAllOrderByCreatedAtDesc();
}