package com.inheaven.PetService.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inheaven.PetService.repository.UserRepository;
import com.inheaven.PetService.exception.UserAlreadyExistsException;
import com.inheaven.PetService.exception.UserNotFoundException;
import com.inheaven.PetService.entity.User;
import com.inheaven.PetService.dto.AuthRequest;
import com.inheaven.PetService.dto.AuthResponse;
import com.inheaven.PetService.dto.UserRegister;
import com.inheaven.PetService.security.JwtService;

import java.util.List;

@Service
public class UserService {
    // Inject các dependencies cần thiết
    private final UserRepository userRepository; // Repository để tương tác với database
    private final PasswordEncoder passwordEncoder; // Encoder để mã hóa mật khẩu
    private final JwtService jwtService; // Service để xử lý JWT

    // Constructor injection
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // Đăng ký user mới
    @Transactional
    public User register(UserRegister registerRequest) {
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username đã tồn tại");
        }

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email đã tồn tại");
        }

        // Tạo user mới từ request
        User user = User.builder()
                .username(registerRequest.getUsername())
                .fullname(registerRequest.getFullname())
                .gender(registerRequest.getGender())
                .email(registerRequest.getEmail())
                .phone(registerRequest.getPhone())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role("USER") // Mặc định role là USER
                .address(registerRequest.getAddress())
                .status("ACTIVE") // Mặc định status là ACTIVE
                .build();

        // Lưu user vào database
        return userRepository.save(user);
    }

    // Đăng nhập và trả về token
    public AuthResponse login(AuthRequest authRequest) {
        // Tìm user theo username
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Không tìm thấy user"));

        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Mật khẩu không chính xác");
        }

        // Tạo token
        String token = jwtService.generateToken(user);

        // Trả về token
        return new AuthResponse(token);
    }

    // Lấy tất cả users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Lấy user theo ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Không tìm thấy user với ID: " + id));
    }

    // Lấy user theo username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Không tìm thấy user với username: " + username));
    }

    // Cập nhật thông tin user
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        // Cập nhật thông tin
        user.setFullname(userDetails.getFullname());
        user.setGender(userDetails.getGender());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setAddress(userDetails.getAddress());
        user.setAvatar(userDetails.getAvatar());

        // Lưu vào database
        return userRepository.save(user);
    }

    // Xóa user
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
