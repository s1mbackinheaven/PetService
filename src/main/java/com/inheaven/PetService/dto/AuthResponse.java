package com.inheaven.PetService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data // Tạo getter, setter tự động
@AllArgsConstructor // Constructor có tham số
public class AuthResponse {
    private String token; // Token JWT trả về sau khi đăng nhập thành công
}
