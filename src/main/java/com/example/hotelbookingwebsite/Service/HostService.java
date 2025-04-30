package com.example.hotelbookingwebsite.Service;

import com.example.hotelbookingwebsite.Model.Booking;
import com.example.hotelbookingwebsite.Model.Hotel;
import com.example.hotelbookingwebsite.Model.Room;
import com.example.hotelbookingwebsite.Repository.BookingRepository;
import com.example.hotelbookingwebsite.Repository.HotelRepository;
import com.example.hotelbookingwebsite.Repository.RoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HostService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // Lấy danh sách phòng theo ID khách sạn
    public List<Room> getRoomsByHotelId(Long hotelId) {
        return roomRepository.findByHotelHid(hotelId);
    }

    // Lấy thông tin 1 khách sạn theo ID
    public Optional<Hotel> getHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId);
    }

    // Lấy danh sách đơn đặt phòng của 1 khách sạn
    public List<Booking> getBookingsByHotelId(Long hotelId) {
        return bookingRepository.findByRoomHotelHid(hotelId);
    }

    // Thêm mới 1 phòng
    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }

    // Cập nhật thông tin khách sạn
    public Hotel updateHotelInfo(Hotel hotel) {
        return hotelRepository.save(hotel);
    }
}
