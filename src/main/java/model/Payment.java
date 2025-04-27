package model;

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

    @Column(nullable = false)
    private String paymentMethod = Constants.UNKNOWN;

    @Column(nullable = false)
    private String status = Constants.UNKNOWN;

    private float amount = 0;

    @OneToOne
    @JoinColumn(name = "bid", referencedColumnName = "bid")  // Foreign key column in Payment referencing Booking's bid
    @JsonBackReference  // Prevent recursion during serialization (optional)
    private Booking booking;  // Each payment belongs to one booking
}
