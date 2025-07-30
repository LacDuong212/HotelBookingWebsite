# HotelBookingWebsite

Dự án HotelBookingWebsite là một hệ thống đặt phòng khách sạn trực tuyến, cho phép người dùng tìm kiếm, đặt phòng, quản lý khách sạn, quản lý người dùng, thanh toán và sử dụng các chương trình khuyến mãi.

## Mục lục

- [Giới thiệu](#giới-thiệu)
- [Tính năng](#tính-năng)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [Cài đặt & Chạy dự án](#cài-đặt--chạy-dự-án)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Đóng góp](#đóng-góp)
- [Liên hệ](#liên-hệ)

---

## Giới thiệu

HotelBookingWebsite là một ứng dụng web cho phép:
- Người dùng tìm kiếm, đặt phòng khách sạn, quản lý tài khoản, xem lịch sử đặt phòng.
- Quản trị viên quản lý người dùng, khách sạn, voucher.
- Chủ khách sạn quản lý khách sạn, phòng, đơn đặt phòng.

## Tính năng

- Đăng ký, đăng nhập, quên mật khẩu
- Tìm kiếm khách sạn, xem chi tiết khách sạn, phòng
- Đặt phòng, thanh toán (tích hợp VNPAY)
- Quản lý tài khoản, chỉnh sửa thông tin cá nhân
- Quản trị viên: quản lý người dùng, khách sạn, voucher
- Chủ khách sạn: quản lý khách sạn, phòng, đơn đặt phòng
- Gửi email xác nhận, OTP

## Cài đặt & Chạy dự án

### Yêu cầu

- Java 17+
- Maven 3.6+
- MySQL/MariaDB

### Cài đặt

1. Clone dự án:
    ```bash
    git clone <repo-url>
    cd HotelBookingWebsite
    ```

2. Cấu hình database trong `src/main/resources/application.properties`:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/hotelbooking
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```

3. Chạy lệnh build:
    ```bash
    ./mvnw clean install
    ```

4. Chạy ứng dụng:
    ```bash
    ./mvnw spring-boot:run
    ```

5. Truy cập: [http://localhost:8080](http://localhost:8080)

## Công nghệ sử dụng

- Spring Boot
- Spring Security
- Thymeleaf
- JPA/Hibernate
- MySQL/MariaDB
- VNPAY (tích hợp thanh toán)
- JavaMail (gửi email)
