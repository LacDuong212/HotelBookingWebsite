package com.example.hotelbookingwebsite.DTO;


import com.example.hotelbookingwebsite.Model.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {

    private Long rid;
    private String description;
    private Float price;
    private String roomName;
    private String status;
    private Hotel hotel;

    private String imageUrl; // URL ảnh đại diện của phòng
}
