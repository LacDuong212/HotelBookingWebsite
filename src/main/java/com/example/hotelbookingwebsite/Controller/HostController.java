package com.example.hotelbookingwebsite.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HostController {

    @GetMapping("/host/add-room")
    public String AddRoom(Model model) {
        return "host/add-room";
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
