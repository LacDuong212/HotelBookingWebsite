package com.example.hotelbookingwebsite.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bid;

    @Column(nullable = false)
    private LocalDateTime checkInDate = LocalDateTime.now();    // default value

    private LocalDateTime checkOutDate;

    @Column(nullable = false)
    private boolean status = Constants.PROMOTION_STATUS.INACTIVE;

    private float totalPrice = 0;

    @ManyToOne
    @JoinColumn(name = "uid")  // Foreign key column for Customer
    @JsonBackReference  // Prevent recursion during serialization (optional)
    private Customer customer;  // A booking belongs to one customer

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("booking")  // Prevent infinite recursion during serialization (optional)
    private Payment payment;  // Each booking has one payment

    @OneToOne
    @JoinColumn(name = "rid", referencedColumnName = "rid")  // Foreign key to Room
    @JsonBackReference  // Prevent recursion during serialization (optional)
    private Room room;  // Each booking is associated with one room
}
