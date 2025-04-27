package model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "managers")
public class Manager {
    @Id
    private Long uid;

    @OneToOne
    @MapsId
    @JoinColumn(name = "uid")
    private User user;

    @OneToOne(mappedBy = "manager")
    @JoinColumn(name = "hid", referencedColumnName = "hid")  // Foreign key column for Hotel
    @JsonBackReference  // Prevent recursion in the Manager entity
    private Hotel hotel;  // A manager is assigned to a hotel

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("manager")  // Prevent infinite recursion during serialization (optional)
    private List<Promotion> promotions;  // A manager can have many promotions
}
