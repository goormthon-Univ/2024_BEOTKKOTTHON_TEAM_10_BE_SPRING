package com.team10.cen.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userid;
    private String password;
    private String name;
    private String ranking;
    private String grade;
    private Date createdAt;
    private Date updatedAt;
    private String regionCityProvince;
    private String regionCityCountyDistrict;
    private String major;
}