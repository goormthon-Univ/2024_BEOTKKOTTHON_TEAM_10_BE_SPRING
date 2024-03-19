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
    @Column(name = "userid") // Specify the column name explicitly
    private String userId;

    private String password;
    private String name;
    private String ranking;
    private String grade;
    private Date createdAt;
    private Date updatedAt;
    private String regionCityProvince;
    private String regionCityCountyDistrict;
    private String major;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_scrapped_scholarships",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "scholarship_id"))
    private Set<Scholarship> scrappedScholarships = new HashSet<>();
}