package com.example.hotelbookingwebsite.Controller;

import com.example.hotelbookingwebsite.Model.Room;
import com.example.hotelbookingwebsite.Repository.HotelRepository;
import com.example.hotelbookingwebsite.Repository.RoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HostController {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    public HostController(HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping("/host/add-room")
    public String showAddRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "host/add-room";
    }

    @PostMapping("/host/add-room")
    public String handleAddRoom(@ModelAttribute Room room) {
        // Giả sử hotelId là 1 (sau này nên lấy từ session hoặc tham số)
        Long hotelId = 1L;
        room.setHotel(hotelRepository.findById(hotelId).orElse(null));
        roomRepository.save(room);
        return "redirect:/host/list-room";
    }

    @GetMapping("/host/edit-info-hotel")
    public String editInfoHotel(Model model) {
        return "host/edit-info-hotel";
    }

    @GetMapping("/host/info-hotel")
    public String InfoHotel(Model model) {
        return "host/info-hotel";
    }

    @GetMapping("/host/list-book-room")
    public String listBookRoom(Model model) {
        return "host/list-book-room";
    }

    @GetMapping("/host/list-room")
    public String listRoom(Model model) {
        return "host/list-room";
    }

    @GetMapping("/host/manage-voucher")
    public String manageVoucher(Model model) {
        return "host/manage-voucher";
    }
}
