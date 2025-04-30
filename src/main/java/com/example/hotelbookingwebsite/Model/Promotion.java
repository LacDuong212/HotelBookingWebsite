package com.example.hotelbookingwebsite.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    @Column(nullable = false)
    private String name = Constants.UNKNOWN;

    private String description = "";

    @Column(nullable = false)
    private float discount = 0;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(nullable = false)
    private boolean status = Constants.PROMOTION_STATUS.INACTIVE;

    @ManyToOne
    @JoinColumn(name = "uid")
    @JsonBackReference
    private Manager manager;  // Each promotion belongs to one manager
}
