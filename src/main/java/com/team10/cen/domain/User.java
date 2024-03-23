package com.team10.cen.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @Column(name = "userId") // Specify the column name explicitly
    private String userId;

    private String password;
    private String name;
    private String ranking;
    private String grade;
    private String regionCityProvince;
    private String regionCityCountryDistrict;
    private String major;
    private boolean onboard;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Save> saves = new HashSet<>();
}