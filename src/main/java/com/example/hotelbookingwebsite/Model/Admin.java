package com.example.hotelbookingwebsite.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admins")
public class Admin {
    @Id
    private Long uid;

    @OneToOne
    @MapsId
    @JoinColumn(name = "uid")
    private User user;
}
