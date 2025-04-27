package com.example.hotelbookingwebsite;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/manage-hotels")
    public String managehotels(Model model) {
        return "admin/manage-hotels";
    }

    @GetMapping("/admin/manage-users")
    public String manageusers(Model model) {
        return "admin/manage-users";
    }
}
