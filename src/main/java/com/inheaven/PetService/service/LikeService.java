package com.inheaven.PetService.service;

import com.inheaven.PetService.dto.LikeRequestDto;
import com.inheaven.PetService.dto.LikeResponseDto;
import com.inheaven.PetService.dto.UserDTO;
import com.inheaven.PetService.entity.Article;
import com.inheaven.PetService.entity.Like;
import com.inheaven.PetService.entity.User;
import com.inheaven.PetService.exception.ResourceNotFoundException;
import com.inheaven.PetService.repository.ArticleRepository;
import com.inheaven.PetService.repository.LikeRepository;
import com.inheaven.PetService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // Đánh dấu đây là một Spring Service
@RequiredArgsConstructor // Tạo constructor có tham số cho các thuộc tính final
public class LikeService {

    private final LikeRepository likeRepository; // Repository để thao tác với bảng Like
    private final UserRepository userRepository; // Repository để thao tác với bảng User
    private final ArticleRepository articleRepository; // Repository để thao tác với bảng Article

    /**
     * Thêm hoặc xóa lượt thích của một người dùng cho một bài viết
     *
     * @param likeRequestDto Thông tin lượt thích
     * @return Thông tin lượt thích nếu đã thêm, null nếu đã xóa
     */
    @Transactional // Đảm bảo tính toàn vẹn của giao dịch
    public LikeResponseDto toggleLike(LikeRequestDto likeRequestDto) {
        // Tìm người dùng theo ID
        User user = userRepository.findById(likeRequestDto.getUserId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found with id: " + likeRequestDto.getUserId()));

        // Tìm bài viết theo ID
        Article article = articleRepository.findById(likeRequestDto.getArticleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Article not found with id: " + likeRequestDto.getArticleId()));

        // Kiểm tra xem người dùng đã thích bài viết hay chưa
        Optional<Like> existingLike = likeRepository.findByUserAndArticle(user, article);

        if (existingLike.isPresent()) {
            // Nếu đã thích, xóa lượt thích
            likeRepository.delete(existingLike.get());
            return null; // Trả về null để biết đã xóa lượt thích
        } else {
            // Nếu chưa thích, thêm lượt thích mới
            Like like = Like.builder()
                    .user(user)
                    .article(article)
                    .build();

            // Lưu lượt thích vào cơ sở dữ liệu
            Like savedLike = likeRepository.save(like);

            // Chuyển đổi từ entity sang DTO và trả về
            return convertToLikeResponseDto(savedLike);
        }
    }

    /**
     * Lấy danh sách người dùng đã thích một bài viết
     *
     * @param articleId ID của bài viết
     * @return Danh sách lượt thích
     */
    public List<LikeResponseDto> getLikesByArticleId(Long articleId) {
        // Tìm bài viết theo ID
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with id: " + articleId));

        // Lấy danh sách lượt thích của bài viết
        List<Like> likes = likeRepository.findByArticle(article);

        // Chuyển đổi danh sách từ entity sang DTO và trả về
        return likes.stream()
                .map(this::convertToLikeResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Lấy số lượng lượt thích của một bài viết
     *
     * @param articleId ID của bài viết
     * @return Số lượng lượt thích
     */
    public long countLikesByArticleId(Long articleId) {
        // Tìm bài viết theo ID
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with id: " + articleId));

        // Đếm số lượng lượt thích của bài viết
        return likeRepository.countByArticle(article);
    }

    /**
     * Kiểm tra xem một người dùng đã thích một bài viết hay chưa
     *
     * @param userId    ID của người dùng
     * @param articleId ID của bài viết
     * @return true nếu đã thích, false nếu chưa thích
     */
    public boolean hasUserLikedArticle(Long userId, Long articleId) {
        // Tìm người dùng theo ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Tìm bài viết theo ID
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with id: " + articleId));

        // Kiểm tra xem người dùng đã thích bài viết hay chưa
        return likeRepository.existsByUserAndArticle(user, article);
    }

    /**
     * Chuyển đổi từ entity Like sang DTO LikeResponseDto
     *
     * @param like Entity Like
     * @return DTO LikeResponseDto
     */
    private LikeResponseDto convertToLikeResponseDto(Like like) {
        return LikeResponseDto.builder()
                .id(like.getId())
                .user(mapUserToDto(like.getUser()))
                .articleId(like.getArticle().getId())
                .createdAt(like.getCreatedAt())
                .build();
    }

    /**
     * Chuyển đổi từ entity User sang DTO UserDTO
     *
     * @param user Entity User
     * @return DTO UserDTO
     */
    private UserDTO mapUserToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .build();
    }
}