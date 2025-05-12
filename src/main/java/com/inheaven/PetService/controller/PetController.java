package com.inheaven.PetService.controller;

import com.inheaven.PetService.dto.PetRequest;
import com.inheaven.PetService.dto.PetResponse;
import com.inheaven.PetService.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Đánh dấu đây là một REST Controller
@RequestMapping("/api/pets") // Định nghĩa base URL cho controller
@CrossOrigin(origins = "*") // Cho phép truy cập từ bất kỳ origin nào
public class PetController {

    private final PetService petService;

    // Constructor injection
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping // Xử lý GET request để lấy danh sách pet
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Chỉ USER và ADMIN mới được
    // xem danh sách pet
    public ResponseEntity<List<PetResponse>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @GetMapping("/{id}") // Xử lý GET request để lấy thông tin một pet
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Chỉ USER và ADMIN mới được
    // xem thông tin pet
    public ResponseEntity<PetResponse> getPetById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }

    @PostMapping // Xử lý POST request để tạo mới pet
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Chỉ USER và ADMIN mới được
    // tạo pet
    public ResponseEntity<PetResponse> createPet(@RequestBody PetRequest petRequest) {
        return new ResponseEntity<>(petService.createPet(petRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}") // Xử lý PUT request để cập nhật thông tin pet
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Chỉ USER và ADMIN mới được
    // cập nhật pet
    public ResponseEntity<PetResponse> updatePet(@PathVariable Long id, @RequestBody PetRequest petRequest) {
        return ResponseEntity.ok(petService.updatePet(id, petRequest));
    }

    @DeleteMapping("/{id}") // Xử lý DELETE request để xóa pet
    // @PreAuthorize("hasRole('ADMIN')") // Chỉ ADMIN mới được xóa pet
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}") // Xử lý GET request để lấy danh sách pet của user
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Chỉ USER và ADMIN mới được
    // xem danh sách pet của user
    public ResponseEntity<List<PetResponse>> getPetsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(petService.getPetsByUserId(userId));
    }

    @PostMapping("/user/{userId}") // Xử lý POST request để tạo mới pet cho user
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Chỉ ADMIN mới được tạo pet
    // cho user khác
    public ResponseEntity<PetResponse> createPetForUser(@PathVariable Long userId, @RequestBody PetRequest petRequest) {
        return new ResponseEntity<>(petService.createPetForUser(userId, petRequest), HttpStatus.CREATED);
    }
}