package com.team10.cen.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Entity
@Getter @Setter
@Table(name = "scholarships")
public class Scholarship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String description2;

    private String description3;

    private String description4;

    private String provider;

    private String startDate;

    private String endDate;

    private String amount;

    private String amount2;

    private String supportRanking;

    private String supportGrade;

    private String supportTarget;

    private String supportTarget2;

    private String supportTarget3;

    private String supportCityProvince;

    private String supportMajor;

    private String requiredDocuments;

    private String site;

    private LocalDateTime createdAt;

    @Transient
    private Long dDay;

    // Getter와 Setter도 추가합니다.

    public Long getDDay() {
        // 현재 날짜와 종료일 간의 차이를 계산하여 D-DAY를 반환합니다.
        if (endDate != null) {
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate now = LocalDate.now();
            return now.until(end, ChronoUnit.DAYS);
        } else {
            return null;
        }
    }

    public void setDDay(Long dDay) {
        this.dDay = dDay;
    }
}