package com.example.hotelbookingwebsite.Controller;

import com.example.hotelbookingwebsite.DTO.HotelDTO;
import com.example.hotelbookingwebsite.DTO.HotelDetailDTO;
import com.example.hotelbookingwebsite.Model.Hotel;
import com.example.hotelbookingwebsite.Model.Promotion;
import com.example.hotelbookingwebsite.Model.User;
import com.example.hotelbookingwebsite.Service.*;
import jakarta.servlet.http.HttpSession;
import com.example.hotelbookingwebsite.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    private final HotelService hotelService;
    private final HotelDetailService hotelDetailService;
    private final RoomService roomService;
    private final BookingService bookingService;
    private final PromotionService promotionService;

    @Autowired
    public HomeController(HotelService hotelService, HotelDetailService hotelDetailService, RoomService roomService, BookingService bookingService, PromotionService promotionService) {
        this.hotelService = hotelService;
        this.hotelDetailService = hotelDetailService;
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.promotionService = promotionService;
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

    @GetMapping("/room-detail/{id}")
    public String roomdetail(@PathVariable("id") long id, Model model) {
        model.addAttribute("room",roomService.getRoomDTOById(id));
        return "web/room-detail";
    }

    @GetMapping("/booking-history")
    public String bookingHistory(HttpSession session,Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser != null) {
            model.addAttribute("upcomingBookings", bookingService.getBookingByUidAndStatus(loggedInUser.getUid(),"UPCOMING"));
            model.addAttribute("confirmedBookings", bookingService.getBookingByUidAndStatus(loggedInUser.getUid(),"CONFIRMED"));
        return "web/booking-history";
        }
        else {
            return "redirect:/signin"; // Chuyển hướng về URL "/signin"
        }
    }
    @GetMapping("/cancel-booking/{id}")
    public String cancelBooking(@PathVariable("id") Long id) {
        bookingService.cancelBooking(id);
        return "redirect:/booking-history"; // chuyển hướng về trang hiển thị danh sách booking
    }
    @GetMapping("/booking-details/{id}")
    public String bookingDetails(@PathVariable("id") long id,Model model) {
        model.addAttribute("booking", bookingService.getBookingBybid(id));
        return "web/booking-details";
    }

    @GetMapping("/booking-payment")
    public String bookingPayment(Model model) {
        return "web/booking-payment";
    }
    @GetMapping("/vouchers")
    public String voucher(Model model) {
        model.addAttribute("promotionactive",promotionService.getPromotionByStatus(true));
        model.addAttribute("promotioninactive",promotionService.getPromotionByStatus(false));
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
    public String updateAccount(@RequestParam("uid") Long uid,
                                @RequestParam("fullname") String fullname,
                                @RequestParam("email") String email,
                                @RequestParam("phoneNumber") String phoneNumber,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        try {
            User currentUser = (User) session.getAttribute("user");

            if (currentUser == null || !currentUser.getUid().equals(uid)) {
                redirectAttributes.addFlashAttribute("error", "Không có quyền cập nhật thông tin này");
                return "redirect:/signin";
            }

            currentUser.setFullname(fullname);
            currentUser.setEmail(email);
            currentUser.setPhoneNumber(phoneNumber);

            User updatedUser = userService.updateUser(currentUser);

            // Update session
            session.setAttribute("user", updatedUser);

            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công");

            // Redirect based on user role
            if ("MANAGER".equals(currentUser.getRole())) {
                return "redirect:/host/account";
            } else {
                return "redirect:/customer/account";
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());

            // Redirect back to edit page based on user role
            if (session.getAttribute("user") != null && "MANAGER".equals(((User)session.getAttribute("user")).getRole())) {
                return "redirect:/host/edit-account";
            } else {
                return "redirect:/customer/edit-account";
            }
        }
    }
}
