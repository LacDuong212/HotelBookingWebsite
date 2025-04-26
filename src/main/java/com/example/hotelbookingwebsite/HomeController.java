package com.example.hotelbookingwebsite;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        return "web/home";
    }
    @GetMapping("/search")
    public String search(Model model) {
        return "web/search_results";
    }

    @GetMapping("/hotel-detail")
    public String hoteldetail(Model model) {
        return "web/hotel-detail";
    }

    @GetMapping("/room-detail")
    public String roomdetail(Model model) {
        return "web/room-detail";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        return "web/signup";
    }
    @GetMapping("/signin")
    public String signin(Model model) {
        return "web/signin";
    }
    @GetMapping("/forgotpassword")
    public String forgotpassword(Model model) {
        return "web/forgotpassword";
    }
    @GetMapping("/booking-history")
    public String bookingHistory(Model model) {
        return "web/booking-history";
    }
    @GetMapping("/booking-details")
    public String bookingDetails(Model model) {
        return "web/booking-details";
    }
    @GetMapping("/booking-payment")
    public String bookingPayment(Model model) {
        return "web/booking-payment";
    }
}
