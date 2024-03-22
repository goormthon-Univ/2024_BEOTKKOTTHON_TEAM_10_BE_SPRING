package com.team10.cen.domain;

import io.micrometer.common.lang.Nullable;
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

    private String supportMajor;

    private String requiredDocuments;

    private String site;

    private LocalDateTime createdAt;
}