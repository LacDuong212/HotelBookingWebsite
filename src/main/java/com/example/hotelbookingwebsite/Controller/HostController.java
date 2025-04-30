package com.example.hotelbookingwebsite.Controller;

import com.example.hotelbookingwebsite.DTO.HotelDTO;
import com.example.hotelbookingwebsite.Model.Images;
import com.example.hotelbookingwebsite.Model.User;
import com.example.hotelbookingwebsite.Service.HotelService;
import com.example.hotelbookingwebsite.Service.ImageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/host")
public class HostController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("newestHotels", hotelService.getNewestHotels());
        model.addAttribute("hcmHotels", hotelService.getTop4NewestHotelsInHCM());
        return "host/home";
    }

    @GetMapping("/add-hotel")
    public String showAddHotelForm(Model model) {
        model.addAttribute("hotelDTO", new HotelDTO());
        return "host/add-hotel";
    }

    @PostMapping("/add-hotel")
    public String addHotel(
            @Valid @ModelAttribute("hotelDTO") HotelDTO hotelDTO,
            HttpSession session,
            BindingResult result,
            @RequestParam("mainImage") MultipartFile mainImage,
            @RequestParam("additionalImages") MultipartFile[] additionalImages,
            RedirectAttributes redirectAttributes
    ) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn chưa đăng nhập.");
            return "redirect:/signin";
        }

        if (result.hasErrors()) {
            return "host/add-hotel";
        }

        try {
            Long hotelId = hotelService.saveHotel(hotelDTO, loggedInUser.getUid());

            if (!mainImage.isEmpty()) {
                String mainImageUrl = imageService.uploadImage(mainImage);
                Images mainImageEntity = new Images();
                mainImageEntity.setImageUrl(mainImageUrl);
                mainImageEntity.setOid(hotelId);
                mainImageEntity.setStt(0);
                imageService.saveImage(mainImageEntity);
            }

            int order = 1;
            for (MultipartFile file : additionalImages) {
                if (file != null && !file.isEmpty()) {
                    String imageUrl = imageService.uploadImage(file);
                    Images imageEntity = new Images();
                    imageEntity.setImageUrl(imageUrl);
                    imageEntity.setOid(hotelId);
                    imageEntity.setStt(order++);
                    imageService.saveImage(imageEntity);
                }
            }

            redirectAttributes.addFlashAttribute("success", "Khách sạn đã được thêm thành công!");
            return "redirect:/host/home";

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tải ảnh lên: " + e.getMessage());
            return "redirect:/host/add-hotel";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm khách sạn: " + e.getMessage());
            return "redirect:/host/add-hotel";
        }
    }

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
