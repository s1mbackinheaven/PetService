package com.inheaven.PetService.service.impl;

import com.inheaven.PetService.dto.PetRequest;
import com.inheaven.PetService.service.PetSuggestionService;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service // Đánh dấu đây là một Service bean
public class PetSuggestionServiceImpl implements PetSuggestionService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String generateSuggestion(PetRequest petRequest) {
        List<String> suggestions = new ArrayList<>();

        suggestions.add(analyzeDiet(petRequest.getType(), petRequest.getBreed(), petRequest.getWeight(),
                petRequest.getDescription()));
        suggestions.add(analyzeHealth(petRequest.getHealthStatus(), petRequest.getHealthHistory(), petRequest.getType(),
                petRequest.getBreed()));
        suggestions.add(analyzeBehavior(petRequest.getDescription(), petRequest.getType(), petRequest.getBreed()));
        suggestions.add(analyzeExercise(petRequest.getType(), petRequest.getBreed(), petRequest.getWeight(),
                petRequest.getDescription()));

        String result = String.join("\\n\\n", suggestions)
                .replace("\n", "\\n");

        return "\"" + result + "\"";
    }

    @Override
    public String analyzeDiet(String type, String breed, double weight, String description) {
        StringBuilder dietSuggestion = new StringBuilder("Đề xuất về chế độ ăn uống:\n");

        // Phân tích dựa trên loại pet
        if (type.equalsIgnoreCase("Dog")) {
            dietSuggestion.append("- Chó cần được ăn 2-3 bữa mỗi ngày\n");
            if (weight < 5) {
                dietSuggestion.append("- Nên cho ăn thức ăn dành cho chó nhỏ, dễ tiêu hóa\n");
            } else if (weight > 20) {
                dietSuggestion.append("- Cần kiểm soát khẩu phần ăn để tránh béo phì\n");
            }
        } else if (type.equalsIgnoreCase("Cat")) {
            dietSuggestion.append("- Mèo nên được cho ăn tự do hoặc chia nhỏ thành nhiều bữa\n");
            dietSuggestion.append("- Đảm bảo luôn có nước sạch\n");
        }

        // Phân tích dựa trên giống
        if (breed != null && !breed.isEmpty()) {
            if (breed.toLowerCase().contains("husky") || breed.toLowerCase().contains("alaska")) {
                dietSuggestion.append("- Cần bổ sung thêm protein và chất béo do đặc tính hoạt động nhiều\n");
            } else if (breed.toLowerCase().contains("persian") || breed.toLowerCase().contains("maine coon")) {
                dietSuggestion.append("- Bổ sung thêm dầu cá để duy trì bộ lông khỏe mạnh\n");
            }
        }

        // Phân tích dựa trên mô tả
        if (description != null && !description.isEmpty()) {
            if (description.toLowerCase().contains("ăn ít") || description.toLowerCase().contains("biếng ăn")) {
                dietSuggestion.append("- Thử đổi loại thức ăn hoặc thêm thức ăn ướt để kích thích ăn uống\n");
            } else if (description.toLowerCase().contains("ăn nhiều")
                    || description.toLowerCase().contains("thèm ăn")) {
                dietSuggestion.append("- Kiểm soát khẩu phần ăn và tăng cường vận động\n");
            }
        }

        return dietSuggestion.toString();
    }

    @Override
    public String analyzeHealth(String healthStatus, String healthHistory, String type, String breed) {
        StringBuilder healthSuggestion = new StringBuilder("Đề xuất về sức khỏe và tiêm phòng:\n");

        // Kiểm tra tiêm phòng
        if (healthHistory == null || !healthHistory.toLowerCase().contains("tiêm phòng")) {
            healthSuggestion.append("- Cần tiêm phòng các bệnh cơ bản:\n");
            if (type.equalsIgnoreCase("Dog")) {
                healthSuggestion.append("  + Dại\n");
                healthSuggestion.append("  + Parvo\n");
                healthSuggestion.append("  + Care\n");
                healthSuggestion.append("  + Ho cũi chó\n");
            } else if (type.equalsIgnoreCase("Cat")) {
                healthSuggestion.append("  + Dại\n");
                healthSuggestion.append("  + FVRCP (Viêm mũi khí quản truyền nhiễm)\n");
                healthSuggestion.append("  + FeLV (Bệnh bạch cầu)\n");
            }
            healthSuggestion.append("- Nên tiêm nhắc lại định kỳ 6-12 tháng/lần\n");
        }

        // Phân tích tình trạng sức khỏe
        if (healthStatus != null && !healthStatus.isEmpty()) {
            if (healthStatus.toLowerCase().contains("ốm") || healthStatus.toLowerCase().contains("bệnh")) {
                healthSuggestion.append("- Cần theo dõi sát sao và tái khám định kỳ\n");
            }
        }

        // Đề xuất dựa trên giống
        if (breed != null && !breed.isEmpty()) {
            if (breed.toLowerCase().contains("persian") || breed.toLowerCase().contains("maine coon")) {
                healthSuggestion.append("- Cần chải lông thường xuyên để tránh búi lông\n");
                healthSuggestion.append("- Kiểm tra và vệ sinh mắt định kỳ\n");
            } else if (breed.toLowerCase().contains("husky") || breed.toLowerCase().contains("alaska")) {
                healthSuggestion.append("- Cần tiêm phòng đầy đủ do tiếp xúc nhiều với môi trường bên ngoài\n");
            }
        }

        return healthSuggestion.toString();
    }

    @Override
    public String analyzeBehavior(String description, String type, String breed) {
        StringBuilder behaviorSuggestion = new StringBuilder("Đề xuất về hành vi và thói quen:\n");

        // Phân tích dựa trên mô tả hành vi
        if (description != null && !description.isEmpty()) {
            if (description.toLowerCase().contains("cắn") || description.toLowerCase().contains("gầm gừ")) {
                behaviorSuggestion.append("- Cần huấn luyện để kiểm soát hành vi hung hăng\n");
                behaviorSuggestion.append("- Tham khảo ý kiến chuyên gia về hành vi thú cưng\n");
            } else if (description.toLowerCase().contains("sợ") || description.toLowerCase().contains("nhút nhát")) {
                behaviorSuggestion.append("- Tạo môi trường an toàn và thoải mái\n");
                behaviorSuggestion.append("- Tăng cường tương tác tích cực\n");
            }
        }

        // Đề xuất dựa trên loại và giống
        if (type.equalsIgnoreCase("Dog")) {
            behaviorSuggestion.append("- Cần được dắt đi dạo ít nhất 2 lần/ngày\n");
            if (breed != null && (breed.toLowerCase().contains("husky") || breed.toLowerCase().contains("alaska"))) {
                behaviorSuggestion.append("- Cần không gian rộng và hoạt động nhiều\n");
                behaviorSuggestion.append("- Huấn luyện từ nhỏ để kiểm soát hành vi\n");
            }
        } else if (type.equalsIgnoreCase("Cat")) {
            behaviorSuggestion.append("- Cung cấp cột cào móng và đồ chơi\n");
            behaviorSuggestion.append("- Tạo không gian leo trèo an toàn\n");
        }

        return behaviorSuggestion.toString();
    }

    @Override
    public String analyzeExercise(String type, String breed, double weight, String description) {
        StringBuilder exerciseSuggestion = new StringBuilder("Đề xuất về tập luyện:\n");

        // Đề xuất dựa trên loại pet
        if (type.equalsIgnoreCase("Dog")) {
            exerciseSuggestion.append("- Dắt đi dạo ít nhất 2 lần/ngày, mỗi lần 15-30 phút\n");
            if (weight > 20) {
                exerciseSuggestion.append("- Tập luyện nhẹ nhàng, tránh vận động mạnh\n");
            } else {
                exerciseSuggestion.append("- Có thể cho chơi các trò chơi vận động\n");
            }
        } else if (type.equalsIgnoreCase("Cat")) {
            exerciseSuggestion.append("- Sử dụng đồ chơi để kích thích vận động\n");
            exerciseSuggestion.append("- Tạo không gian leo trèo an toàn\n");
        }

        // Đề xuất dựa trên giống
        if (breed != null && !breed.isEmpty()) {
            if (breed.toLowerCase().contains("husky") || breed.toLowerCase().contains("alaska")) {
                exerciseSuggestion.append("- Cần vận động mạnh ít nhất 1 giờ/ngày\n");
                exerciseSuggestion.append("- Cho chạy bộ hoặc kéo xe nhẹ\n");
            } else if (breed.toLowerCase().contains("persian") || breed.toLowerCase().contains("maine coon")) {
                exerciseSuggestion.append("- Tập luyện nhẹ nhàng, tránh vận động mạnh\n");
                exerciseSuggestion.append("- Tập trung vào các bài tập linh hoạt\n");
            }
        }

        return exerciseSuggestion.toString();
    }
}