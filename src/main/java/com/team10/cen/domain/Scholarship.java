package com.team10.cen.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
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

    private String provider;

    private String startDate;

    private String endDate;

    private String amount;

    private String supportRanking;

    private String supportGrade;

    private String supportCityProvince;

    private String supportCityCountyDistrict;

    private String supportMajor;

    private String requiredDocuments;

    private String site;

    private String supportTarget;

    private Integer dDay;

    // Callback method to calculate and set D-day before persisting
    @PrePersist
    public void calculateAndSetDday() {
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
    }
}