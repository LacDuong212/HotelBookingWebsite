package com.example.hotelbookingwebsite.Service;

import com.example.hotelbookingwebsite.DTO.HotelDetailDTO;
import com.example.hotelbookingwebsite.DTO.RoomDTO;
import com.example.hotelbookingwebsite.Model.Hotel;
import com.example.hotelbookingwebsite.Model.Images;
import com.example.hotelbookingwebsite.Repository.HotelRepository;
import com.example.hotelbookingwebsite.Repository.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelDetailService {
    private final ImagesRepository imagesRepository;
    private final HotelRepository hotelRepository;
    @Autowired
    public HotelDetailService(ImagesRepository imagesRepository, HotelRepository hotelRepository) {
        this.imagesRepository = imagesRepository;
        this.hotelRepository = hotelRepository;
    }
    private HotelDetailDTO convertToDTO(Hotel hotel) {
        return new HotelDetailDTO(
                hotel.getHid(),
                hotel.getName(),
                hotel.getAddress(),
                hotel.getDescription(),
                getHotelImage(hotel.getHid()),
                hotel.getManager(),
                convertToRoomDTO(hotel)
        );
    }
    private List<Images> getRoomImage(Long hid,long rid) {
        long id = hid*100 + rid;
        return imagesRepository.findImagesByHid(id);

    }
    public List<RoomDTO> convertToRoomDTO(Hotel hotel) {
        return hotel.getRooms().stream().map(room -> {
            RoomDTO dto = new RoomDTO();
            dto.setRid(room.getRid());
            dto.setRoomName(room.getRoomName());
            dto.setDescription(room.getDescription());
            dto.setStatus(room.getStatus());
            dto.setPrice((double) room.getPrice());
            dto.setHid(hotel.getHid());

            // Lấy ảnh đại diện từ room
            List<Images> imageUrls = getRoomImage(hotel.getHid(),room.getRid());
            dto.setImageUrl(imageUrls.isEmpty() ? null : "/images/" + imageUrls.get(0).getImageUrl());

            return dto;
        }).collect(Collectors.toList());
    }
    public List<Images> getHotelImage(Long hid) {
        List<Images> images = imagesRepository.findImagesByHid(hid);
        images.forEach(img -> img.setImageUrl("/images/" + img.getImageUrl()));
        return images;
    }

    public HotelDetailDTO getHotelById(long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow();
        return convertToDTO(hotel);
    }

}
