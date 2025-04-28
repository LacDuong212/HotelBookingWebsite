package com.example.hotelbookingwebsite.Repository;

import com.example.hotelbookingwebsite.Model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    // Lấy 4 khách sạn mới nhất
    List<Hotel> findTop4ByOrderByCreatedDateDesc();

    // Lấy tất cả khách sạn (không cần điều kiện status)
    List<Hotel> findAllByOrderByCreatedDateDesc();
}
