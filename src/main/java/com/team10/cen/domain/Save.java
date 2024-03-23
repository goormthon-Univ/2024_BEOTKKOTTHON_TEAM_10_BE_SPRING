package com.team10.cen.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "saves")
public class Save {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userUserId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scholarshipId")
    private Scholarship scholarship;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        SAVED,
        APPLYING,
        COMPLETED
    }

    public Save() {
        this.status = Status.SAVED;
    }
}