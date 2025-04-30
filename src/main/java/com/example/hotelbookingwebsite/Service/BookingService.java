package com.example.hotelbookingwebsite.Service;

import com.example.hotelbookingwebsite.DTO.BookingDTO;
import com.example.hotelbookingwebsite.Model.Booking;
import com.example.hotelbookingwebsite.Model.Customer;
import com.example.hotelbookingwebsite.Repository.BookingRepository;
import com.example.hotelbookingwebsite.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final RoomService roomService;
    @Autowired
    public BookingService(BookingRepository bookingRepository, CustomerRepository customerRepository, RoomService roomService) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.roomService = roomService;
    }
    public BookingDTO getBookingBybid(long bid) {
        return convertToDTO(bookingRepository.findById(bid).get());
    }
    public List<BookingDTO> getBookingByUidAndStatus(long uid, String status) {
        Customer customer = customerRepository.findById(uid)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + uid));

        List<Booking> bookings = bookingRepository.findByCustomerAndStatus(customer, status);

        return bookings.stream()
                .map(this::convertToDTO)
                .toList();
    }
    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBid(booking.getBid());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setStatus(booking.getStatus());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setCustomer(booking.getCustomer());
        dto.setPayment(booking.getPayment());
        dto.setRoom(roomService.convertToRoomDTO(booking.getRoom()));
        return dto;
    }
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }
    public void deleteBooking(long id) {
        bookingRepository.deleteById(id);
    }
}
