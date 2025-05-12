package com.inheaven.PetService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Tự động tạo getter, setter, toString, equals, hashCode
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
public class UserDTO {
    private Long id; // ID của người dùng
    private String username; // Tên người dùng
    private String fullname; // Họ tên đầy đủ
    private String gender; // Giới tính
    private String email; // Email
    private String phone; // Số điện thoại
    private String address; // Địa chỉ
    private String avatar; // Ảnh đại diện
    private String status; // Trạng thái
}