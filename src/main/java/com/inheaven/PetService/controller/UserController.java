package com.inheaven.PetService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inheaven.PetService.service.UserService;
import com.inheaven.PetService.dto.AuthRequest;
import com.inheaven.PetService.dto.AuthResponse;
import com.inheaven.PetService.dto.UserRegister;
import com.inheaven.PetService.entity.User;
import com.inheaven.PetService.dto.UserDTO;
import com.inheaven.PetService.dto.ChangePasswordRequest;
import com.inheaven.PetService.dto.MessageResponse;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController // Đánh dấu đây là REST Controller
@RequestMapping("/api/users") // Định nghĩa base URL cho controller
@CrossOrigin(origins = "*") // Cho phép truy cập từ bất kỳ origin nào
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
    // @PreAuthorize("isAuthenticated()") // Chỉ cần xác thực
    public ResponseEntity<User> getCurrentUser() {
        // Lấy username từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    // Lấy tất cả users (chỉ admin)
    @GetMapping // Xử lý GET request để lấy danh sách users
    // @PreAuthorize("hasRole('ADMIN')") // Chỉ ADMIN mới được xem danh sách users
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Lấy tất cả bác sĩ
    @GetMapping("/doctors")
    public ResponseEntity<List<User>> getAllDoctors() {
        return ResponseEntity.ok(userService.getAllDoctors());
    }

    // Lấy user theo ID (chỉ admin)
    @GetMapping("/{id}") // Xử lý GET request để lấy thông tin một user
    // @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id") //
    // ADMIN hoặc chính user đó mới được xem
    // thông tin
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Cập nhật thông tin user
    @PutMapping("/{id}") // Xử lý PUT request để cập nhật thông tin user
    // @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id") //
    // ADMIN hoặc chính user đó mới được cập
    // nhật thông tin
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    // Xóa user (chỉ admin)
    @DeleteMapping("/{id}") // Xử lý DELETE request để xóa user
    // @PreAuthorize("hasRole('ADMIN')") // Chỉ ADMIN mới được xóa user
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/username/{username}") // Xử lý GET request để lấy thông tin user theo username
    // @PreAuthorize("hasRole('ADMIN') or #username ==
    // authentication.principal.username") // ADMIN hoặc chính user đó mới
    // được xem thông tin
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    // Thay đổi mật khẩu
    @PutMapping("/{id}/change-password") // Xử lý PUT request để thay đổi mật khẩu
    // @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id") //
    // ADMIN hoặc chính user đó mới được thay đổi mật khẩu
    public ResponseEntity<MessageResponse> changePassword(@PathVariable Long id,
            @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(userService.changePassword(id, request));
    }
}
