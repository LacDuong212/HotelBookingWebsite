package com.example.hotelbookingwebsite.Controller;

import com.example.hotelbookingwebsite.DTO.HotelDTO;
import com.example.hotelbookingwebsite.Model.Hotel;
import com.example.hotelbookingwebsite.Model.Images;
import com.example.hotelbookingwebsite.Model.User;
import com.example.hotelbookingwebsite.Repository.ImagesRepository;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/host")
public class HostController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImagesRepository imagesRepository;

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
            @RequestParam(value = "additionalImages", required = false) MultipartFile[] additionalImages,
            @RequestParam(value = "extraImages", required = false) MultipartFile[] extraImages,
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

            // Xử lý ảnh chính
            if (!mainImage.isEmpty()) {
                String mainImageUrl = imageService.uploadImage(mainImage);
                Images mainImageEntity = new Images();
                mainImageEntity.setImageUrl(mainImageUrl);
                mainImageEntity.setOid(hotelId);
                mainImageEntity.setStt(0);
                imageService.saveImage(mainImageEntity);
            }

            // Xử lý các ảnh thumbnail cơ bản
            List<MultipartFile> allAdditionalImages = new ArrayList<>();

            // Thêm ảnh từ additionalImages nếu có
            if (additionalImages != null) {
                for (MultipartFile file : additionalImages) {
                    if (file != null && !file.isEmpty()) {
                        allAdditionalImages.add(file);
                    }
                }
            }

            // Thêm ảnh từ extraImages nếu có
            if (extraImages != null) {
                for (MultipartFile file : extraImages) {
                    if (file != null && !file.isEmpty()) {
                        allAdditionalImages.add(file);
                    }
                }
            }

            // Lưu tất cả các ảnh bổ sung
            int order = 1;
            for (MultipartFile file : allAdditionalImages) {
                String imageUrl = imageService.uploadImage(file);
                Images imageEntity = new Images();
                imageEntity.setImageUrl(imageUrl);
                imageEntity.setOid(hotelId);
                imageEntity.setStt(order++);
                imageService.saveImage(imageEntity);
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

    @GetMapping("/info-hotel")
    public String InfoHotel(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return "redirect:/signin";
        }

        Hotel hotel = hotelService.getHotelByHostUid(loggedInUser.getUid());
        List<Images> imagesList = imagesRepository.findByOidOrderBySttAsc(hotel.getHid());

        session.setAttribute("hotel", hotel);
        model.addAttribute("hotel", hotel);
        session.setAttribute("imagesList", imagesList);
        model.addAttribute("imagesList", imagesList);

        return "host/info-hotel";
    }

    @GetMapping("/edit-info-hotel/{id}")
    public String editInfoHotel(@PathVariable("id") Long id, Model model, HttpSession session) {
        Hotel hotel = hotelService.getHotelById(id);
        List<Images> imagesList = imagesRepository.findByOidOrderBySttAsc(hotel.getHid());

        model.addAttribute("hotel", hotel);
        session.setAttribute("hotel", hotel);
        model.addAttribute("imagesList", imagesList);
        session.setAttribute("imagesList", imagesList);
        return "host/edit-info-hotel";
    }

    @PostMapping("/update-hotel")
    public String updateHotel(
            @RequestParam("hotelId") Long hotelId,
            @RequestParam("hotelName") String hotelName,
            @RequestParam("hotelAddress") String hotelAddress,
            @RequestParam("hotelDescription") String hotelDescription,
            @RequestParam(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestParam(value = "thumbnailImage0", required = false) MultipartFile thumbnailImage0,
            @RequestParam(value = "thumbnailImage1", required = false) MultipartFile thumbnailImage1,
            @RequestParam(value = "thumbnailImage2", required = false) MultipartFile thumbnailImage2,
            @RequestParam(value = "thumbnailImageNew1", required = false) MultipartFile thumbnailImageNew1,
            @RequestParam(value = "thumbnailImageNew2", required = false) MultipartFile thumbnailImageNew2,
            @RequestParam(value = "thumbnailImageNew3", required = false) MultipartFile thumbnailImageNew3,
            @RequestParam(value = "additionalImage", required = false) MultipartFile[] additionalImages,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        try {
            Hotel hotel = hotelService.getHotelById(hotelId);

            hotel.setName(hotelName);
            hotel.setAddress(hotelAddress);
            hotel.setDescription(hotelDescription);

            hotelService.updateHotel(hotel);

            if (mainImage != null && !mainImage.isEmpty()) {
                Images mainImageEntity = imagesRepository.findByOidAndStt(hotelId, 0);
                if (mainImageEntity != null) {
                    String newImageUrl = imageService.uploadImage(mainImage);
                    mainImageEntity.setImageUrl(newImageUrl);
                    imageService.saveImage(mainImageEntity);
                } else {
                    String newImageUrl = imageService.uploadImage(mainImage);
                    Images newMainImage = new Images();
                    newMainImage.setOid(hotelId);
                    newMainImage.setStt(0);
                    newMainImage.setImageUrl(newImageUrl);
                    imageService.saveImage(newMainImage);
                }
            }

            processExistingThumbnail(hotelId, 1, thumbnailImage0);
            processExistingThumbnail(hotelId, 2, thumbnailImage1);
            processExistingThumbnail(hotelId, 3, thumbnailImage2);
            processNewThumbnail(hotelId, thumbnailImageNew1);
            processNewThumbnail(hotelId, thumbnailImageNew2);
            processNewThumbnail(hotelId, thumbnailImageNew3);

            if (additionalImages != null) {
                Integer maxStt = imagesRepository.findMaxSttByOid(hotelId);
                int nextStt = (maxStt != null) ? maxStt + 1 : 4;

                for (MultipartFile file : additionalImages) {
                    if (file != null && !file.isEmpty()) {
                        String imageUrl = imageService.uploadImage(file);
                        Images imageEntity = new Images();
                        imageEntity.setImageUrl(imageUrl);
                        imageEntity.setOid(hotelId);
                        imageEntity.setStt(nextStt++);
                        imageService.saveImage(imageEntity);
                    }
                }
            }

            redirectAttributes.addFlashAttribute("success", "Cập nhật khách sạn thành công!");
            return "redirect:/host/info-hotel";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", "Cập nhật khách sạn thành công!");
            return "redirect:/host/edit-info-hotel/" + hotelId;
        }
    }

    // Phương thức hỗ trợ xử lý thumbnail đã tồn tại
    private void processExistingThumbnail(Long hotelId, int stt, MultipartFile thumbnailImage) throws IOException {
        if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
            Images existingImage = imagesRepository.findByOidAndStt(hotelId, stt);
            if (existingImage != null) {
                String newImageUrl = imageService.uploadImage(thumbnailImage);
                existingImage.setImageUrl(newImageUrl);
                imageService.saveImage(existingImage);
            } else {
                String imageUrl = imageService.uploadImage(thumbnailImage);
                Images newImage = new Images();
                newImage.setOid(hotelId);
                newImage.setStt(stt);
                newImage.setImageUrl(imageUrl);
                imageService.saveImage(newImage);
            }
        }
    }

    private void processNewThumbnail(Long hotelId, MultipartFile thumbnailImage) throws IOException {
        if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
            List<Integer> existingStts = imagesRepository.findSttByOid(hotelId);
            int nextStt = 1;
            while (existingStts.contains(nextStt) && nextStt < 4) {
                nextStt++;
            }

            if (nextStt < 4) {
                String imageUrl = imageService.uploadImage(thumbnailImage);
                Images newImage = new Images();
                newImage.setOid(hotelId);
                newImage.setStt(nextStt);
                newImage.setImageUrl(imageUrl);
                imageService.saveImage(newImage);
            }
        }
    }

    @GetMapping("/add-room")
    public String AddRoom(Model model) {
        return "host/add-room";
    }

    @GetMapping("/list-book-room")
    public String listBookRoom(Model model) {
        return "host/list-book-room";
    }

    @GetMapping("/list-room")
    public String listRoom(Model model) {
        return "host/list-room";
    }

    @GetMapping("/manage-voucher")
    public String manageVoucher(Model model) {
        return "host/manage-voucher";
    }
}
