package com.example.hotelbookingwebsite.Service;

import com.example.hotelbookingwebsite.DTO.HotelDTO;
import com.example.hotelbookingwebsite.Model.Hotel;
import com.example.hotelbookingwebsite.Model.Images;
import com.example.hotelbookingwebsite.Repository.HotelRepository;
import com.example.hotelbookingwebsite.Repository.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;
    private final ImagesRepository imagesRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository, ImagesRepository imagesRepository) {
        this.hotelRepository = hotelRepository;
        this.imagesRepository = imagesRepository;
    }

    public List<HotelDTO> getNewestHotels() {
        return hotelRepository.findTop4ByOrderByCreatedDateDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<HotelDTO> getAllHotels() {
        return hotelRepository.findAllByOrderByCreatedDateDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private HotelDTO convertToDTO(Hotel hotel) {
//        String imageUrl = getFirstHotelImageUrl(hotel.getHid());
//        return new HotelDTO(
//                hotel.getHid(),
//                hotel.getName(),
//                hotel.getAddress(),
//                hotel.getDescription(),
//                imageUrl
//        );
        return null;
    }

    private String getFirstHotelImageUrl(Long hotelId) {
        List<Images> images = imagesRepository.findImagesByHid(hotelId);
        return null;
//        return images.isEmpty() ?
//                "https://images.unsplash.com/photo-1566073771259-6a8506099945?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"
//                : images.getFirst().getImageUrl();
    }
}
