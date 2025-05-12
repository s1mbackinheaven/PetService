package com.inheaven.PetService.service;

import com.inheaven.PetService.dto.PetRequest;
import com.inheaven.PetService.dto.PetResponse;
import java.util.List;

public interface PetService {
    // Lấy danh sách tất cả pet
    List<PetResponse> getAllPets();

    // Lấy thông tin pet theo id
    PetResponse getPetById(Long id);

    // Tạo mới pet
    PetResponse createPet(PetRequest petRequest);

    // Cập nhật thông tin pet
    PetResponse updatePet(Long id, PetRequest petRequest);

    // Xóa pet
    void deletePet(Long id);

    // Lấy danh sách pet của user
    List<PetResponse> getPetsByUserId(Long userId);

    // Tạo mới pet cho user
    PetResponse createPetForUser(Long userId, PetRequest petRequest);
}