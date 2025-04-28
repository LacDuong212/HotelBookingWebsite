package com.example.hotelbookingwebsite.DTO;

import lombok.Data;

@Data
public class HotelDTO {
    private Long id;
    private String name;
    private String address;
    private String description;
    private String firstImageUrl;

    public HotelDTO(Long id, String name, String address, String description, String firstImageUrl) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.firstImageUrl = firstImageUrl;
    }
}
