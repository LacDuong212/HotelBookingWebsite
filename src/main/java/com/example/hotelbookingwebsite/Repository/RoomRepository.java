package com.example.hotelbookingwebsite.Repository;

import com.example.hotelbookingwebsite.Model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
