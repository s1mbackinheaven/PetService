package com.inheaven.PetService.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtAuthenticationService {
    // Phương thức xác thực token và trả về UserDetails
    UserDetails authenticateToken(String token);

    // Phương thức tạo token mới
    String generateToken(UserDetails userDetails);

    // Phương thức kiểm tra token có hợp lệ không
    boolean validateToken(String token, UserDetails userDetails);

    // Phương thức lấy username từ token
    String getUsernameFromToken(String token);
}