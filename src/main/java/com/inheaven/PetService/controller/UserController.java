package com.inheaven.PetService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inheaven.PetService.service.UserService;
import com.inheaven.PetService.dto.AuthRequest;
import com.inheaven.PetService.dto.AuthResponse;
import com.inheaven.PetService.dto.UserRegister;
import com.inheaven.PetService.entity.User;

import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController // Đánh dấu đây là REST Controller
@RequestMapping("/api/user") // Prefix cho tất cả các endpoint
@RequiredArgsConstructor // Tự động inject các dependency
public class UserController {

    private final UserService userService; // Service để xử lý logic

    // Đăng ký user mới
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRegister request) {
        return ResponseEntity.ok(userService.register(request));
    }

    // Đăng nhập và lấy token
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    // Lấy thông tin user hiện tại
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String token) {
        // Lấy username từ token
        String username = token.substring(7); // Bỏ "Bearer " prefix
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    // Lấy tất cả users (chỉ admin)
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Lấy user theo ID (chỉ admin)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Cập nhật thông tin user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    // Xóa user (chỉ admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
