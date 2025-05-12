package com.inheaven.PetService.service;

import com.inheaven.PetService.dto.UserDTO;
import com.inheaven.PetService.dto.ArticleRequestDto;
import com.inheaven.PetService.dto.ArticleResponseDto;
import com.inheaven.PetService.entity.Article;
import com.inheaven.PetService.entity.User;
import com.inheaven.PetService.exception.ResourceNotFoundException;
import com.inheaven.PetService.repository.ArticleRepository;
import com.inheaven.PetService.repository.CommentRepository;
import com.inheaven.PetService.repository.LikeRepository;
import com.inheaven.PetService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service // Đánh dấu đây là một Spring Service
@RequiredArgsConstructor // Tạo constructor có tham số cho các thuộc tính final
public class ArticleService {

        private final ArticleRepository articleRepository; // Repository để thao tác với bảng Article
        private final UserRepository userRepository; // Repository để thao tác với bảng User
        private final CommentRepository commentRepository; // Repository để thao tác với bảng Comment
        private final LikeRepository likeRepository; // Repository để thao tác với bảng Like

        /**
         * Tạo một bài viết mới
         *
         * @param articleRequestDto Thông tin bài viết cần tạo
         * @return Thông tin bài viết đã tạo
         */
        @Transactional // Đảm bảo tính toàn vẹn của giao dịch
        public ArticleResponseDto createArticle(ArticleRequestDto articleRequestDto) {
                // Tìm người dùng theo ID
                User user = userRepository.findById(articleRequestDto.getUserId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "User not found with id: " + articleRequestDto.getUserId()));

                // Tạo đối tượng Article từ ArticleRequestDto
                Article article = Article.builder()
                                .subject(articleRequestDto.getSubject())
                                .title(articleRequestDto.getTitle())
                                .content(articleRequestDto.getContent())
                                .user(user)
                                .active(false)
                                .build();

                // Lưu bài viết vào cơ sở dữ liệu
                Article savedArticle = articleRepository.save(article);

                // Chuyển đổi từ entity sang DTO và trả về
                return convertToArticleResponseDto(savedArticle);
        }

        /**
         * Lấy thông tin của một bài viết theo ID
         *
         * @param articleId ID của bài viết cần lấy thông tin
         * @return Thông tin bài viết
         */
        public ArticleResponseDto getArticleById(Long articleId) {
                // Tìm bài viết theo ID
                Article article = articleRepository.findById(articleId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Article not found with id: " + articleId));

                // Chuyển đổi từ entity sang DTO và trả về
                return convertToArticleResponseDto(article);
        }

        /**
         * Lấy danh sách tất cả các bài viết
         *
         * @return Danh sách bài viết
         */
        public List<ArticleResponseDto> getAllArticles() {
                // Lấy tất cả bài viết từ cơ sở dữ liệu
                List<Article> articles = articleRepository.findAll();

                // Chuyển đổi danh sách từ entity sang DTO và trả về
                return articles.stream()
                                .map(this::convertToArticleResponseDto)
                                .collect(Collectors.toList());
        }

        /**
         * Lấy danh sách bài viết đã được duyệt
         *
         * @return Danh sách bài viết đã được duyệt
         */
        public List<ArticleResponseDto> getApprovedArticles() {
                // Lấy danh sách bài viết có trạng thái active = true
                List<Article> articles = articleRepository.findByActiveTrue();

                // Chuyển đổi danh sách từ entity sang DTO và trả về
                return articles.stream()
                                .map(this::convertToArticleResponseDto)
                                .collect(Collectors.toList());
        }

        /**
         * Lấy danh sách bài viết chưa được duyệt
         *
         * @return Danh sách bài viết chưa được duyệt
         */
        public List<ArticleResponseDto> getPendingArticles() {
                // Lấy danh sách bài viết có trạng thái active = false
                List<Article> articles = articleRepository.findByActiveFalse();

                // Chuyển đổi danh sách từ entity sang DTO và trả về
                return articles.stream()
                                .map(this::convertToArticleResponseDto)
                                .collect(Collectors.toList());
        }

        /**
         * Kiểm duyệt bài viết
         *
         * @param articleId ID của bài viết cần kiểm duyệt
         * @param status    Trạng thái kiểm duyệt (true: duyệt, false: không duyệt)
         * @return Thông tin bài viết đã cập nhật
         */
        @Transactional
        public ArticleResponseDto moderateArticle(Long articleId, boolean status) {
                // Tìm bài viết theo ID
                Article article = articleRepository.findById(articleId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Article not found with id: " + articleId));

                // Cập nhật trạng thái kiểm duyệt
                article.setActive(status);

                // Lưu bài viết đã cập nhật vào cơ sở dữ liệu
                Article updatedArticle = articleRepository.save(article);

                // Chuyển đổi từ entity sang DTO và trả về
                return convertToArticleResponseDto(updatedArticle);
        }

        /**
         * Cập nhật thông tin của một bài viết
         *
         * @param articleId         ID của bài viết cần cập nhật
         * @param articleRequestDto Thông tin bài viết mới
         * @return Thông tin bài viết đã cập nhật
         */
        @Transactional
        public ArticleResponseDto updateArticle(Long articleId, ArticleRequestDto articleRequestDto) {
                // Tìm bài viết theo ID
                Article article = articleRepository.findById(articleId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Article not found with id: " + articleId));

                // Cập nhật thông tin bài viết
                article.setSubject(articleRequestDto.getSubject());
                article.setTitle(articleRequestDto.getTitle());
                article.setContent(articleRequestDto.getContent());

                // Lưu bài viết đã cập nhật vào cơ sở dữ liệu
                Article updatedArticle = articleRepository.save(article);

                // Chuyển đổi từ entity sang DTO và trả về
                return convertToArticleResponseDto(updatedArticle);
        }

        /**
         * Xóa một bài viết
         *
         * @param articleId ID của bài viết cần xóa
         */
        @Transactional
        public void deleteArticle(Long articleId) {
                // Tìm bài viết theo ID
                Article article = articleRepository.findById(articleId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Article not found with id: " + articleId));

                // Xóa bài viết
                articleRepository.delete(article);
        }

        /**
         * Lấy danh sách bài viết của một người dùng
         *
         * @param userId ID của người dùng
         * @return Danh sách bài viết
         */
        public List<ArticleResponseDto> getArticlesByUserId(Long userId) {
                // Tìm người dùng theo ID
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

                // Lấy danh sách bài viết của người dùng
                List<Article> articles = articleRepository.findByUser(user);

                // Chuyển đổi danh sách từ entity sang DTO và trả về
                return articles.stream()
                                .map(this::convertToArticleResponseDto)
                                .collect(Collectors.toList());
        }

        /**
         * Lấy danh sách bài viết theo chủ đề
         *
         * @param subject Chủ đề
         * @return Danh sách bài viết
         */
        public List<ArticleResponseDto> getArticlesBySubject(String subject) {
                // Lấy danh sách bài viết theo chủ đề
                List<Article> articles = articleRepository.findBySubject(subject);

                // Chuyển đổi danh sách từ entity sang DTO và trả về
                return articles.stream()
                                .map(this::convertToArticleResponseDto)
                                .collect(Collectors.toList());
        }

        /**
         * Tìm kiếm bài viết theo từ khóa
         *
         * @param keyword Từ khóa tìm kiếm
         * @return Danh sách bài viết
         */
        public List<ArticleResponseDto> searchArticles(String keyword) {
                // Tìm kiếm bài viết theo từ khóa trong tiêu đề hoặc nội dung
                List<Article> articles = articleRepository.findByTitleContainingOrContentContaining(keyword, keyword);

                // Chuyển đổi danh sách từ entity sang DTO và trả về
                return articles.stream()
                                .map(this::convertToArticleResponseDto)
                                .collect(Collectors.toList());
        }

        /**
         * Tìm kiếm bài viết đã được duyệt theo từ khóa
         *
         * @param keyword Từ khóa tìm kiếm
         * @return Danh sách bài viết đã được duyệt
         */
        public List<ArticleResponseDto> searchApprovedArticles(String keyword) {
                // Tìm kiếm bài viết theo từ khóa trong tiêu đề hoặc nội dung
                List<Article> articles = articleRepository
                                .findByTitleContainingOrContentContainingAndActiveTrue(keyword, keyword);

                // Chuyển đổi danh sách từ entity sang DTO và trả về
                return articles.stream()
                                .map(this::convertToArticleResponseDto)
                                .collect(Collectors.toList());
        }

        /**
         * Chuyển đổi từ entity Article sang DTO ArticleResponseDto
         *
         * @param article Entity Article
         * @return DTO ArticleResponseDto
         */
        private ArticleResponseDto convertToArticleResponseDto(Article article) {
                // Đếm số lượng lượt thích cho bài viết
                int likeCount = (int) likeRepository.countByArticle(article);

                return ArticleResponseDto.builder()
                                .id(article.getId())
                                .subject(article.getSubject())
                                .title(article.getTitle())
                                .creationDate(article.getCreationDate())
                                .content(article.getContent())
                                .user(mapUserToDto(article.getUser()))
                                .likeCount(likeCount)
                                .active(article.getActive())
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