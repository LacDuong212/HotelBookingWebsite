package com.example.hotelbookingwebsite.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {

    private Long rid;
    private String description;
    private Double price;
    private String roomName;
    private String status;
    private Long hid;

    private String imageUrl; // URL ảnh đại diện của phòng
}
