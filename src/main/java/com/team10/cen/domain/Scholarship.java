package com.team10.cen.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private String supportCityCountyDistrict;

    private String supportMajor;

    private String requiredDocuments;

    private String site;

    private Integer dDay;

    private LocalDateTime createdAt;

    // Callback method to calculate and set D-day before persisting
    @PrePersist
    public void calculateAndSetDdayAndCreatedAt() {
        if (endDate != null && !endDate.isEmpty()) {
            // 현재 날짜 가져오기
            LocalDate currentDate = LocalDate.now();
            System.out.println("Current Date: " + currentDate);

            // 마감일을 LocalDate로 변환
            LocalDate endDateLocalDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println("Parsed End Date: " + endDateLocalDate);

            // 현재 날짜와 마감일 간의 차이 계산
            int dday = (int) currentDate.until(endDateLocalDate).getDays();
            System.out.println("Calculated D-day: " + dday);

            // Scholarship 엔티티에 D-day 정보 설정
            setDDay(dday);
        } else {
            System.out.println("End Date is null or empty");
        }

        // 추가: 생성 시간 설정
        setCreatedAt(LocalDateTime.now());
    }
}