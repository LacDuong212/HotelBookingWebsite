package com.example.hotelbookingwebsite.Repository;

import com.example.hotelbookingwebsite.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRoomHotelHid(Long hid);
}