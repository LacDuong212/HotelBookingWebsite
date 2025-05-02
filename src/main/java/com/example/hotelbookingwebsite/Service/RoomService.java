package com.example.hotelbookingwebsite.Service;

import com.example.hotelbookingwebsite.DTO.RoomDTO;
import com.example.hotelbookingwebsite.Model.Hotel;
import com.example.hotelbookingwebsite.Model.Images;
import com.example.hotelbookingwebsite.Model.Room;
import com.example.hotelbookingwebsite.Repository.ImagesRepository;
import com.example.hotelbookingwebsite.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final ImagesRepository imagesRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(ImagesRepository imagesRepository, RoomRepository roomRepository) {
        this.imagesRepository = imagesRepository;
        this.roomRepository = roomRepository;
    }
    public RoomDTO getRoomDTOById(Long rid) {
        Room room = roomRepository.findById(rid)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng với ID: " + rid));
        return convertToRoomDTO(room);
    }

    public List<RoomDTO> getAllRoomByHotel(Hotel hotel) {
        return hotel.getRooms().stream()
                .map(this::convertToRoomDTO)
                .collect(Collectors.toList());
    }
    public RoomDTO convertToRoomDTO(Room room) {
            RoomDTO dto = new RoomDTO();
            dto.setRid(room.getRid());
            dto.setRoomName(room.getRoomName());
            dto.setDescription(room.getDescription());
            dto.setStatus(room.getStatus());
            dto.setPrice((double) room.getPrice());
            dto.setHotel(room.getHotel());

            // Lấy ảnh đại diện từ room
            List<Images> imageUrls = getRoomImage(room.getRid());
            dto.setImageUrl(imageUrls.isEmpty() ? null : "/images/" + imageUrls.get(0).getImageUrl());

            return dto;
    }
    private List<Images> getRoomImage(long rid) {
        return imagesRepository.findImagesByRid(rid);

    }
    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
    }
}
