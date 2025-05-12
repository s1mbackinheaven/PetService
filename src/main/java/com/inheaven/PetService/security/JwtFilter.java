package com.inheaven.PetService.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component // Đánh dấu đây là một component
@RequiredArgsConstructor // Tự động inject các dependency
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtService jwtService; // Service xử lý JWT
    private final UserDetailsService userDetailsService; // Service load user details

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization"); // Lấy header Authorization

        // Kiểm tra header Authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy token từ header
        final String jwt = authHeader.substring(7); // Bỏ "Bearer " prefix

        String username = null;
        try {
            // Trích xuất username từ token
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            logger.error("JwtFilter: Lỗi khi trích xuất username từ token: {}", e.getMessage());
        }

        // Kiểm tra username và authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username); // Load user details

            // Kiểm tra token hợp lệ
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()); // Tạo authentication token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Set details
                SecurityContextHolder.getContext().setAuthentication(authToken); // Set authentication
            } else {
                logger.warn("JwtFilter: Token không hợp lệ cho user: {}", username);
            }
        } else {
            if (username == null) {
                logger.warn("JwtFilter: Không thể trích xuất username từ token");
            }
        }

        filterChain.doFilter(request, response);
    }
}