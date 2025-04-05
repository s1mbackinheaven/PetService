package com.inheaven.PetService.config;

import com.inheaven.PetService.security.JwtAuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;

@Component // Đánh dấu đây là component để Spring quản lý
@RequiredArgsConstructor // Tự động inject constructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtAuthenticationService jwtAuthenticationService; // Service xử lý xác thực JWT

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization"); // Lấy header Authorization

        if (authHeader != null && authHeader.startsWith("Bearer ")) { // Kiểm tra nếu token bắt đầu với "Bearer "
            String token = authHeader.substring(7); // Lấy token từ header

            if (SecurityContextHolder.getContext().getAuthentication() == null) { // Kiểm tra nếu chưa có authentication
                UserDetails userDetails = jwtAuthenticationService.authenticateToken(token); // Xác thực token

                if (userDetails != null) { // Nếu xác thực thành công
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()); // Tạo authentication token
                    SecurityContextHolder.getContext().setAuthentication(authToken); // Đặt authentication vào context
                }
            }
        }
        filterChain.doFilter(request, response); // Tiếp tục chuỗi filter
    }
}
