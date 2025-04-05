package com.inheaven.PetService.dto;

import lombok.Data;

@Data // Tạo getter, setter tự động
public class AuthRequest {
    private String username; // Tên người dùng
    private String password; // Mật khẩu người dùng
}
