package com.example.hotelbookingwebsite.Repository;

import com.example.hotelbookingwebsite.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
