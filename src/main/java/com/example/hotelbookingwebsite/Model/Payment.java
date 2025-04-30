package com.example.hotelbookingwebsite.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pmid;

    private String paymentMethod = Constants.UNKNOWN;

    private String status = Constants.UNKNOWN;

    private float amount = 0;

    @OneToOne(mappedBy = "payment")
    @JsonBackReference
    private Booking booking;  // Each payment belongs to one booking
}
