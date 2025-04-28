package com.example.hotelbookingwebsite.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;

    @Column(nullable = false)
    private String roomName = Constants.UNKNOWN;

    private String description = "";

    @Column(nullable = false)
    private String status = Constants.ROOM_STATUS.UNAVAILABLE;

    @Column(nullable = false)
    private float price = 0;

    @ManyToOne
    @JoinColumn(name = "hid")  // Foreign key column for Hotel
    @JsonBackReference  // Prevent recursion during serialization (optional)
    private Hotel hotel;  // Each room belongs to one hotel

    @OneToOne(mappedBy = "room")
    @JsonIgnoreProperties("room")  // Prevent infinite recursion during serialization (optional)
    private Booking booking;  // Each room is associated with one booking
}
