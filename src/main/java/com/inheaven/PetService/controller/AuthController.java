package com.inheaven.PetService.controller;

import com.inheaven.PetService.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Endpoint để tạo token mới với secret key hiện tại
     * 
     * @param username Tên người dùng
     * @return Token JWT mới
     */

    /**
     * Endpoint để kiểm tra token
     * 
     * @param token Token JWT cần kiểm tra
     * @return Kết quả kiểm tra token
     */
    @PostMapping("/check-token")
    public ResponseEntity<Map<String, Object>> checkToken(@RequestParam String token) {
        logger.info("AuthController: Yêu cầu kiểm tra token");

        try {
            // Kiểm tra token một cách chi tiết
            Map<String, Object> validationResult = jwtService.validateToken(token);

            // Kiểm tra token mà không cần xác thực chữ ký
            Map<String, Object> noSignatureValidation = jwtService.validateTokenWithoutSignature(token);

            // Kết hợp kết quả
            Map<String, Object> result = new HashMap<>();
            result.put("withSignature", validationResult);
            result.put("withoutSignature", noSignatureValidation);

            logger.info("AuthController: Đã kiểm tra token thành công");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("AuthController: Lỗi khi kiểm tra token: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Không thể kiểm tra token: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Endpoint để debug token
     * 
     * @param token Token JWT cần debug
     * @return Thông tin chi tiết về token
     */
    @PostMapping("/debug-token")
    public ResponseEntity<Map<String, Object>> debugToken(@RequestParam String token) {
        logger.info("AuthController: Yêu cầu debug token");

        try {
            // Debug token
            Map<String, Object> debugResult = jwtService.debugToken(token);

            logger.info("AuthController: Đã debug token thành công");
            return ResponseEntity.ok(debugResult);
        } catch (Exception e) {
            logger.error("AuthController: Lỗi khi debug token: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Không thể debug token: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}