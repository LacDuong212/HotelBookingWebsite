package com.example.hotelbookingwebsite.Repository;

import com.example.hotelbookingwebsite.Model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // Lấy danh sách phòng theo ID khách sạn
    List<Room> findByHotelHid(Long hid);


}
