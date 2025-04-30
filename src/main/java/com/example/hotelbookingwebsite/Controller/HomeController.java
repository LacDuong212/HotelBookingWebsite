package com.example.hotelbookingwebsite.Controller;

import com.example.hotelbookingwebsite.Model.User;
import com.example.hotelbookingwebsite.Service.HotelService;
import com.example.hotelbookingwebsite.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    private final HotelService hotelService;

    @Autowired
    public HomeController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("newestHotels", hotelService.getNewestHotels());
        model.addAttribute("hcmHotels", hotelService.getTop4NewestHotelsInHCM());
        return "web/home";
    }

    @GetMapping("/hotel-detail")
    public String hoteldetail(Model model) {
        return "web/hotel-detail";
    }

    @GetMapping("/room-detail")
    public String roomdetail(Model model) {
        return "web/room-detail";
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

    @GetMapping({"/host/account", "/customer/account"})
    public String Account(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/signin";
        }

        model.addAttribute("user", loggedInUser);
        return "web/account";
    }

    @GetMapping({"/host/edit-account", "/customer/edit-account"})
    public String editAccount(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return "redirect:/signin";
        }

        model.addAttribute("user", loggedInUser);
        return "web/edit-account";
    }

    @PostMapping({"/host/update-account", "/customer/update-account"})
    @ResponseBody
    public Map<String, Object> updateAccount(@RequestParam("uid") Long uid,
                                             @RequestParam("fullname") String fullname,
                                             @RequestParam("email") String email,
                                             @RequestParam("phoneNumber") String phoneNumber,
                                             HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            User currentUser = (User) session.getAttribute("user");

            if (currentUser == null || !currentUser.getUid().equals(uid)) {
                response.put("success", false);
                response.put("message", "Không có quyền cập nhật thông tin này");
                return response;
            }

            currentUser.setFullname(fullname);
            currentUser.setEmail(email);
            currentUser.setPhoneNumber(phoneNumber);

            User updatedUser = userService.updateUser(currentUser);

            session.setAttribute("loggedInUser", updatedUser);

            response.put("success", true);
            response.put("message", "Cập nhật thông tin thành công");

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }
}
