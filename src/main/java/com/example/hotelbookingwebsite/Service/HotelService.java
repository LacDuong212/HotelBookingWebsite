package com.example.hotelbookingwebsite.Service;

import com.example.hotelbookingwebsite.DTO.HotelDTO;
import com.example.hotelbookingwebsite.Model.Hotel;
import com.example.hotelbookingwebsite.Model.Images;
import com.example.hotelbookingwebsite.Repository.HotelRepository;
import com.example.hotelbookingwebsite.Repository.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        String imageUrl = getFirstHotelImageUrl(hotel.getHid());
        return new HotelDTO(
                hotel.getHid(),
                hotel.getName(),
                hotel.getAddress(),
                hotel.getDescription(),
                imageUrl
        );
    }

    private String getFirstHotelImageUrl(Long hotelId) {
        List<Images> images = imagesRepository.findImagesByHid(hotelId);
        return "/images/" + images.getFirst().getImageUrl();  // Relative path to your ImageController
    }

    public List<HotelDTO> getTop4NewestHotelsInHCM() {
        List<Hotel> hcmHotels = hotelRepository.findTop4NewestHotelsInHCM();

        return hcmHotels.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<HotelDTO> searchHotelsByAddress(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Hotel> hotelPage = hotelRepository.findByAddressContainingIgnoreCase(query, pageable);

        return hotelPage.map(this::convertToDTO);
    }

    public Page<HotelDTO> getAllHotels(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Hotel> hotelPage = hotelRepository.findAll(pageable);

        return hotelPage.map(this::convertToDTO);
    }
}
