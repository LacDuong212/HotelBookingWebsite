package model;

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
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hid;

    @Column(nullable = false)
    private String name = Constants.UNKNOWN;

    @Column(nullable = false)
    private String address = Constants.UNKNOWN;

    private String description = Constants.UNKNOWN;

    private float rating = 0;

    private boolean status = Constants.HOTEL_STATUS.INACTIVE;

    @OneToOne(mappedBy = "hotel")
    @JsonIgnoreProperties("hotel")  // Prevent infinite recursion during serialization (optional)
    private Manager manager;  // A hotel is managed by one manager

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("hotel")  // Prevent infinite recursion during serialization (optional)
    private List<Room> rooms;  // A hotel can have many rooms
}
