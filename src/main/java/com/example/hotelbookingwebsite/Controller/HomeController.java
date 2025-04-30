package com.example.hotelbookingwebsite.Controller;

import com.example.hotelbookingwebsite.DTO.HotelDTO;
import com.example.hotelbookingwebsite.DTO.HotelDetailDTO;
import com.example.hotelbookingwebsite.Model.Hotel;
import com.example.hotelbookingwebsite.Service.HotelDetailService;
import com.example.hotelbookingwebsite.Service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
    private final HotelService hotelService;
    private final HotelDetailService hotelDetailService;

    @Autowired
    public HomeController(HotelService hotelService, HotelDetailService hotelDetailService) {
        this.hotelService = hotelService;
        this.hotelDetailService = hotelDetailService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("newestHotels", hotelService.getNewestHotels());
        model.addAttribute("hcmHotels", hotelService.getTop4NewestHotelsInHCM());
        return "web/home";
    }

    @GetMapping("/hotel-detail/{id}")
    public String hoteldetail(@PathVariable("id") long id, Model model) {
        model.addAttribute("hotel", hotelDetailService.getHotelById(id));
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
    @GetMapping("/voucher")
    public String voucher(Model model) {
        return "web/voucher";
    }

    @GetMapping("/account")
    public String Account(Model model) {
        return "web/account";
    }
}
