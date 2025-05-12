package com.inheaven.PetService.service.impl;

import com.inheaven.PetService.dto.PetRequest;
import com.inheaven.PetService.dto.PetResponse;
import com.inheaven.PetService.entity.Pet;
import com.inheaven.PetService.entity.User;
import com.inheaven.PetService.repository.PetRepository;
import com.inheaven.PetService.repository.UserRepository;
import com.inheaven.PetService.service.PetService;
import com.inheaven.PetService.service.PetSuggestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service // Đánh dấu đây là một Service bean
@Transactional // Đảm bảo tính toàn vẹn dữ liệu
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PetSuggestionService petSuggestionService;

    // Constructor injection
    public PetServiceImpl(PetRepository petRepository, UserRepository userRepository,
            PetSuggestionService petSuggestionService) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.petSuggestionService = petSuggestionService;
    }

    @Override
    public List<PetResponse> getAllPets() {
        // Lấy tất cả pet từ database và chuyển đổi sang DTO
        return petRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PetResponse getPetById(Long id) {
        // Tìm pet theo id và chuyển đổi sang DTO
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + id));
        return convertToResponse(pet);
    }

    @Override
    public PetResponse createPet(PetRequest petRequest) {
        // Chuyển đổi DTO sang Entity
        Pet pet = convertToEntity(petRequest);

        // Tạo đề xuất dựa trên thông tin pet
        String suggestion = petSuggestionService.generateSuggestion(petRequest);
        pet.setSuggestion(suggestion);

        // Lưu vào database
        Pet savedPet = petRepository.save(pet);
        return convertToResponse(savedPet);
    }

    @Override
    public PetResponse updatePet(Long id, PetRequest petRequest) {
        // Tìm pet cần cập nhật
        Pet existingPet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + id));

        // Cập nhật thông tin từ DTO vào Entity
        BeanUtils.copyProperties(petRequest, existingPet, "id");

        // Cập nhật đề xuất dựa trên thông tin mới
        String suggestion = petSuggestionService.generateSuggestion(petRequest);
        existingPet.setSuggestion(suggestion);

        // Lưu vào database
        Pet updatedPet = petRepository.save(existingPet);
        return convertToResponse(updatedPet);
    }

    @Override
    public void deletePet(Long id) {
        // Xóa pet theo id
        petRepository.deleteById(id);
    }

    @Override
    public List<PetResponse> getPetsByUserId(Long userId) {
        // Lấy danh sách pet của user từ database
        return petRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PetResponse createPetForUser(Long userId, PetRequest petRequest) {
        // Tìm user theo id
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Chuyển đổi DTO sang Entity
        Pet pet = convertToEntity(petRequest);

        // Gán user cho pet
        pet.setUser(user);

        // Tạo đề xuất dựa trên thông tin pet
        String suggestion = petSuggestionService.generateSuggestion(petRequest);
        pet.setSuggestion(suggestion);

        // Lưu vào database
        Pet savedPet = petRepository.save(pet);
        return convertToResponse(savedPet);
    }

    // Phương thức chuyển đổi từ Entity sang DTO
    private PetResponse convertToResponse(Pet pet) {
        PetResponse response = new PetResponse();
        BeanUtils.copyProperties(pet, response);
        return response;
    }

    // Phương thức chuyển đổi từ DTO sang Entity
    private Pet convertToEntity(PetRequest request) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(request, pet);
        return pet;
    }
}