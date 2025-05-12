package com.inheaven.PetService.service;

import com.inheaven.PetService.dto.PetRequest;

public interface PetSuggestionService {
    // Phương thức chính để tạo đề xuất dựa trên thông tin pet
    String generateSuggestion(PetRequest petRequest);

    // Phương thức phân tích chế độ ăn uống
    String analyzeDiet(String type, String breed, double weight, String description);

    // Phương thức phân tích sức khỏe và tiêm phòng
    String analyzeHealth(String healthStatus, String healthHistory, String type, String breed);

    // Phương thức phân tích hành vi và thói quen
    String analyzeBehavior(String description, String type, String breed);

    // Phương thức phân tích chế độ tập luyện
    String analyzeExercise(String type, String breed, double weight, String description);
}