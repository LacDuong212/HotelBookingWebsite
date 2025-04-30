package com.example.hotelbookingwebsite.Controller;

import com.example.hotelbookingwebsite.DTO.HotelDTO;
import com.example.hotelbookingwebsite.DTO.UserDTO;
import com.example.hotelbookingwebsite.Service.HotelService;
import com.example.hotelbookingwebsite.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private HotelService hotelService;

    @GetMapping("/manage-hotels")
    public String managehotels(Model model) {
        List<HotelDTO> hotelDTOList = hotelService.getAllHotels();
        model.addAttribute("hotels", hotelDTOList);
        return "admin/manage-hotels";
    }

    @DeleteMapping("/delete-hotel/{hotelId}")
    @ResponseBody
    public ResponseEntity<?> deleteHotel(@PathVariable Long hotelId) {
        try {
            System.out.println(hotelId);
            //hotelService.deleteHotelById(hotelId);
            return ResponseEntity.ok().body(Collections.singletonMap("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Lỗi khi xóa khách sạn: " + e.getMessage()));
        }
    }

    @GetMapping("/manage-users")
    public String manageusers(Model model) {
        List<UserDTO> userDTOList = userService.getAllUser();
        model.addAttribute("users", userDTOList);
        return "admin/manage-users";
    }

    @PostMapping("/update-users")
    @ResponseBody
    public Map<String, Object> updateUsers(@RequestBody Map<String, List<UserDTO>> payload) {
        List<UserDTO> userDTOList = payload.get("users");

        List<UserDTO> updatedUsers = userService.updateUsers(userDTOList);
        Map<String, Object> response = new HashMap<>();
        if (updatedUsers != null && !updatedUsers.isEmpty()) {
            response.put("success", true);
            response.put("updatedUsers", updatedUsers);
        } else {
            response.put("success", false);
            response.put("message", "No users were updated.");
        }

        return response;
    }

    @DeleteMapping("/delete-user/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok().body(Collections.singletonMap("success", true));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "User not found"));
        }
    }
}
