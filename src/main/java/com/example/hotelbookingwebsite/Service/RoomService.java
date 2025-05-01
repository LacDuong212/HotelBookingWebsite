package com.example.hotelbookingwebsite.Service;

import com.example.hotelbookingwebsite.DTO.RoomDTO;
import com.example.hotelbookingwebsite.Model.Hotel;
import com.example.hotelbookingwebsite.Model.Images;
import com.example.hotelbookingwebsite.Model.Room;
import com.example.hotelbookingwebsite.Repository.ImagesRepository;
import com.example.hotelbookingwebsite.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final ImagesRepository imagesRepository;
    private final RoomRepository roomRepository;
    private final ImageService imageService;

    @Autowired
    public RoomService(ImagesRepository imagesRepository, RoomRepository roomRepository, ImageService imageService) {
        this.imagesRepository = imagesRepository;
        this.roomRepository = roomRepository;
        this.imageService = imageService;
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
    public Room saveRoom(String roomName, float price, String description, MultipartFile[] images) throws IOException {
        // Tạo đối tượng Room và gán thông tin từ form vào
        Room room = new Room();
        room.setRoomName(roomName);
        room.setPrice(price);
        room.setDescription(description);
        room.setStatus("available");  // Trạng thái mặc định

        // Lưu phòng vào cơ sở dữ liệu
        Room savedRoom = roomRepository.save(room);

        // Xử lý và lưu ảnh
        for (int i = 0; i < images.length; i++) {
            if (!images[i].isEmpty()) {
                // Lưu ảnh vào thư mục và lấy URL
                String imageUrl = imageService.uploadImage(images[i]);

                // Lưu thông tin ảnh vào bảng Images
                Images imageEntity = new Images();
                imageEntity.setImageUrl(imageUrl);  // URL ảnh
                imageEntity.setOid(savedRoom.getRid());  // Liên kết ảnh với phòng
                imageEntity.setStt(i);  // Thứ tự ảnh

                imagesRepository.save(imageEntity);
            }
        }

        return savedRoom;
    }
    private List<Images> getRoomImage(long rid) {
        return imagesRepository.findImagesByRid(rid);

    }
}
