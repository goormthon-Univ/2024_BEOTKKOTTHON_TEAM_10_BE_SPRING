package com.team10.cen.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
}