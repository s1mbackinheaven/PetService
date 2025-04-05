package com.inheaven.PetService.security;

import com.inheaven.PetService.entity.User;
import com.inheaven.PetService.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service // Đánh dấu là service để Spring quản lý
@RequiredArgsConstructor // Tự động inject constructor
public class JwtAuthenticationServiceImpl implements JwtAuthenticationService {

    private final JwtService jwtService; // Service để xử lý JWT
    private final CustomUserDetailsService userDetailsService; // Service để load user

    @Override
    public UserDetails authenticateToken(String token) {
        // Lấy username từ token
        String username = getUsernameFromToken(token);

        // Load user từ database
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Kiểm tra token hợp lệ
        if (validateToken(token, userDetails)) {
            return userDetails;
        }

        return null;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        // Chuyển đổi UserDetails thành User
        User user = new User();
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setRole(userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", ""));
        return jwtService.generateToken(user);
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        return jwtService.validateToken(token, userDetails);
    }

    @Override
    public String getUsernameFromToken(String token) {
        return jwtService.getUsernameFromToken(token);
    }
}