package com.example.hotelbookingwebsite.Controller;

import com.example.hotelbookingwebsite.DTO.BookingDTO;
import com.example.hotelbookingwebsite.DTO.HotelDTO;
import com.example.hotelbookingwebsite.DTO.RoomDTO;
import com.example.hotelbookingwebsite.Model.*;
import com.example.hotelbookingwebsite.Repository.ImagesRepository;
import com.example.hotelbookingwebsite.Repository.ManagerRepository;
import com.example.hotelbookingwebsite.Repository.PromotionRepository;
import com.example.hotelbookingwebsite.Repository.RoomRepository;
import com.example.hotelbookingwebsite.Service.*;
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
import java.util.Optional;

@Controller
@RequestMapping("/host")
public class HostController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ManagerRepository managerRepository;

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
        if (hotel == null) {
            model.addAttribute("error", "Không tìm thấy thông tin khách sạn.");
            return "redirect:/host/home";
        }

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
            @RequestParam(value = "extraImages", required = false) MultipartFile[] extraImages,
            @RequestParam(value = "additionalImages", required = false) MultipartFile[] additionalImages,
            RedirectAttributes redirectAttributes,
            HttpSession session,
            Model model
    ) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn chưa đăng nhập.");
            return "redirect:/signin";
        }

        try {
            Hotel hotel = hotelService.getHotelById(hotelId);
            hotel.setName(hotelName);
            hotel.setAddress(hotelAddress);
            hotel.setDescription(hotelDescription);
            hotelService.updateHotel(hotel);

            boolean imagesUpdated = false;

            // Process main image
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
                imagesUpdated = true;
            }

            // Process individual thumbnails (for updating existing thumbnail images)
            if (thumbnailImage0 != null && !thumbnailImage0.isEmpty()) {
                updateOrCreateImage(thumbnailImage0, hotelId, 1);
                imagesUpdated = true;
            }

            if (thumbnailImage1 != null && !thumbnailImage1.isEmpty()) {
                updateOrCreateImage(thumbnailImage1, hotelId, 2);
                imagesUpdated = true;
            }

            if (thumbnailImage2 != null && !thumbnailImage2.isEmpty()) {
                updateOrCreateImage(thumbnailImage2, hotelId, 3);
                imagesUpdated = true;
            }

            // Collect all additional images from different sources
            List<MultipartFile> allAdditionalImages = new ArrayList<>();

            // Add images from extraImages (for new thumbnails in empty slots)
            if (extraImages != null) {
                for (MultipartFile file : extraImages) {
                    if (file != null && !file.isEmpty()) {
                        allAdditionalImages.add(file);
                        imagesUpdated = true;
                    }
                }
            }

            // Add images from additionalImages (for "Thêm ảnh" button)
            if (additionalImages != null) {
                for (MultipartFile file : additionalImages) {
                    if (file != null && !file.isEmpty()) {
                        allAdditionalImages.add(file);
                        imagesUpdated = true;
                    }
                }
            }

            // Save all additional images
            if (!allAdditionalImages.isEmpty()) {
                // Get the maximum STT value for this hotel
                Integer maxStt = imagesRepository.findMaxSttByOid(hotelId);
                int nextStt = (maxStt != null) ? maxStt + 1 : 1;

                for (MultipartFile file : allAdditionalImages) {
                    String imageUrl = imageService.uploadImage(file);
                    Images imageEntity = new Images();
                    imageEntity.setImageUrl(imageUrl);
                    imageEntity.setOid(hotelId);
                    imageEntity.setStt(nextStt++);
                    imageService.saveImage(imageEntity);
                }
            }

            // Refresh the image list if any images were updated
            if (imagesUpdated) {
                List<Images> updatedImagesList = imagesRepository.findByOidOrderBySttAsc(hotelId);
                session.setAttribute("imagesList", updatedImagesList);
                model.addAttribute("imagesList", updatedImagesList);
            }

            redirectAttributes.addFlashAttribute("success", "Cập nhật khách sạn thành công!");
            return "redirect:/host/info-hotel";

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tải ảnh lên: " + e.getMessage());
            return "redirect:/host/edit-info-hotel/" + hotelId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật khách sạn: " + e.getMessage());
            return "redirect:/host/edit-info-hotel/" + hotelId;
        }
    }

    // Helper method to update an existing image or create a new one at a specific position
    private void updateOrCreateImage(MultipartFile imageFile, Long hotelId, int position) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            return;
        }

        // Check if an image exists at this position
        Images existingImage = imagesRepository.findByOidAndStt(hotelId, position);
        String newImageUrl = imageService.uploadImage(imageFile);

        if (existingImage != null) {
            // Update existing image
            existingImage.setImageUrl(newImageUrl);
            imageService.saveImage(existingImage);
        } else {
            // Create new image at this position
            Images newImage = new Images();
            newImage.setOid(hotelId);
            newImage.setStt(position);
            newImage.setImageUrl(newImageUrl);
            imageService.saveImage(newImage);
        }
    }

    // New method to show confirmation page before deleting an image
    @GetMapping("/remove-image-confirm")
    public String confirmRemoveImage(@RequestParam("id") Long imageId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/signin";
        }

        try {
            Images image = imagesRepository.findById(imageId).orElse(null);
            if (image == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy hình ảnh.");
                return "redirect:/host/info-hotel";
            }

            // Don't allow deleting main image (STT = 0)
            if (image.getStt() == 0) {
                redirectAttributes.addFlashAttribute("error", "Không thể xóa ảnh chính của khách sạn.");
                return "redirect:/host/edit-info-hotel/" + image.getOid();
            }

            // Get hotel for the return path
            Hotel hotel = hotelService.getHotelById(image.getOid());
            if (hotel == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin khách sạn.");
                return "redirect:/host/home";
            }

            // Set up confirmation data
            model.addAttribute("showConfirmation", true);
            model.addAttribute("imageToDelete", imageId);
            model.addAttribute("hotel", hotel);

            // Get all images for the hotel to display on the page
            List<Images> imagesList = imagesRepository.findByOidOrderBySttAsc(hotel.getHid());
            model.addAttribute("imagesList", imagesList);

            return "host/edit-info-hotel";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xử lý yêu cầu: " + e.getMessage());
            return "redirect:/host/info-hotel";
        }
    }

    // Add this method to actually perform the deletion
    @GetMapping("/delete-image")
    public String deleteImage(@RequestParam("id") Long imageId, RedirectAttributes redirectAttributes, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn chưa đăng nhập.");
            return "redirect:/signin";
        }

        try {
            Images image = imagesRepository.findById(imageId).orElse(null);
            if (image == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy hình ảnh.");
                return "redirect:/host/info-hotel";
            }

            // Don't allow deleting main image (STT = 0)
            if (image.getStt() == 0) {
                redirectAttributes.addFlashAttribute("error", "Không thể xóa ảnh chính của khách sạn.");
                return "redirect:/host/edit-info-hotel/" + image.getOid();
            }

            Long hotelId = image.getOid();

            // Delete the image
            imagesRepository.deleteById(imageId);

            // Reindex the STT values to maintain continuity
            List<Images> remainingImages = imagesRepository.findByOidOrderBySttAsc(hotelId);
            for (int i = 0; i < remainingImages.size(); i++) {
                Images img = remainingImages.get(i);
                // Skip the main image (STT = 0)
                if (img.getStt() > 0) {
                    img.setStt(i); // Adjust index as needed
                    imageService.saveImage(img);
                }
            }

            // Update the image list in session
            List<Images> updatedImagesList = imagesRepository.findByOidOrderBySttAsc(hotelId);
            session.setAttribute("imagesList", updatedImagesList);

            redirectAttributes.addFlashAttribute("success", "Xóa ảnh thành công!");
            return "redirect:/host/edit-info-hotel/" + hotelId;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa ảnh: " + e.getMessage());
            return "redirect:/host/info-hotel";
        }
    }

    @GetMapping("/add-room")
    public String showAddRoomForm(Model model) {
        model.addAttribute("room", new Room());  // Khởi tạo đối tượng phòng mới
        return "host/add-room";  // Trả về trang thêm phòng
    }

    @PostMapping("/add-room")
    public String addRoom(
            @ModelAttribute("room") Room room,
            @RequestParam("images") MultipartFile[] images,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn chưa đăng nhập.");
            return "web/signin";
        }

        try {
            // Lấy khách sạn của người dùng
            Hotel hotel = hotelService.getHotelByHostUid(loggedInUser.getUid());
            if (hotel == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách sạn của bạn.");
                return "host/home";
            }

            // Gán khách sạn cho phòng
            room.setHotel(hotel);

            // Lưu phòng vào cơ sở dữ liệu
            Room savedRoom = roomRepository.save(room);  // Lưu phòng và lấy ID

            // Xử lý ảnh
            for (int i = 0; i < images.length; i++) {
                MultipartFile image = images[i];
                if (!image.isEmpty()) {
                    // Lưu ảnh vào hệ thống (thư mục)
                    String imageUrl = imageService.uploadImage(image); // Lưu ảnh và nhận URL

                    // Tạo đối tượng Images và lưu vào cơ sở dữ liệu
                    Images imageEntity = new Images();
                    imageEntity.setImageUrl(imageUrl);  // Lưu URL ảnh
                    imageEntity.setOid(savedRoom.getRid());  // Gán oid là ID của phòng
                    imageEntity.setStt(i);  // Gán thứ tự ảnh

                    imageService.saveImage(imageEntity);  // Lưu ảnh vào bảng Images
                }
            }

            redirectAttributes.addFlashAttribute("success", "Phòng đã được thêm thành công!");
            return "host/list-room";  // Chuyển hướng đến danh sách phòng

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm phòng: " + e.getMessage());
            return "host/add-room";
        }
    }

    @GetMapping("/list-book-room")
    public String listBookedRooms(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "web/signin";  // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }

        // Lấy khách sạn của người dùng
        Hotel hotel = hotelService.getHotelByHostUid(loggedInUser.getUid());
        if (hotel == null) {
            model.addAttribute("error", "Không tìm thấy khách sạn của bạn.");
            return "host/home";
        }

        // Lấy danh sách các booking đã đặt với trạng thái 'confirmed' (hoặc trạng thái khác nếu cần)
        List<BookingDTO> bookedRooms = bookingService.getBookingByUidAndStatus(loggedInUser.getUid(), "confirmed");
        model.addAttribute("bookedRooms", bookedRooms);

        return "host/list-book-room";  // Trả về trang hiển thị danh sách phòng đã đặt
    }


    @GetMapping("/list-room")
    public String listRooms(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "web/signin";  // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }

        // Lấy khách sạn của người dùng
        Hotel hotel = hotelService.getHotelByHostUid(loggedInUser.getUid());
        if (hotel == null) {
            model.addAttribute("error", "Không tìm thấy khách sạn của bạn.");
            return "host/home";
        }

        // Lấy danh sách phòng của khách sạn thông qua RoomService
        List<RoomDTO> rooms = roomService.getAllRoomByHotel(hotel);
        model.addAttribute("rooms", rooms);  // Trả về danh sách phòng

        return "host/list-room";  // Trả về trang hiển thị danh sách phòng
    }

    @GetMapping("/manage-voucher")
    public String manageVoucher(Model model) {
        List<Promotion> validPromotions = promotionService.getValidPromotions();
        List<Promotion> expiredPromotions = promotionService.getExpiredPromotions();

        model.addAttribute("validPromotions", validPromotions);
        model.addAttribute("expiredPromotions", expiredPromotions);

        return "host/manage-voucher";
    }

    @PostMapping("/voucher/add")
    public String addPromotion(@ModelAttribute("newPromotion") Promotion promotion,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/signin";
        }

        Optional<Manager> optionalManager = managerRepository.findById(loggedInUser.getUid());
        if (optionalManager.isEmpty()) {
            return "redirect:/signin";
        }

        promotion.setManager(optionalManager.get());
        promotionRepository.save(promotion);

        redirectAttributes.addFlashAttribute("successMessage", "Mã giảm giá đã được thêm thành công!");

        return "redirect:/host/manage-voucher";
    }

    @GetMapping("/voucher/delete")
    public String deletePromotion(@RequestParam("id") Long promotionId,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/signin";
        }

        Optional<Manager> optionalManager = managerRepository.findById(loggedInUser.getUid());
        if (optionalManager.isEmpty()) {
            return "redirect:/signin";
        }

        Optional<Promotion> optionalPromotion = promotionRepository.findById(promotionId);
        if (optionalPromotion.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy mã giảm giá!");
            return "redirect:/host/manage-voucher";
        }

        Promotion promotion = optionalPromotion.get();

        if (!promotion.getManager().getUid().equals(loggedInUser.getUid())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa mã giảm giá này!");
            return "redirect:/host/manage-voucher";
        }

        promotionRepository.delete(promotion);
        redirectAttributes.addFlashAttribute("successMessage", "Mã giảm giá đã được xóa thành công!");

        return "redirect:/host/manage-voucher";
    }
}
