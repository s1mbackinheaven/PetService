package com.inheaven.PetService.controller;

import com.inheaven.PetService.dto.ArticleRequestDto;
import com.inheaven.PetService.dto.ArticleResponseDto;
import com.inheaven.PetService.dto.CommentRequestDto;
import com.inheaven.PetService.dto.CommentResponseDto;
import com.inheaven.PetService.dto.LikeRequestDto;
import com.inheaven.PetService.dto.LikeResponseDto;
import com.inheaven.PetService.dto.MessageResponse;
import com.inheaven.PetService.service.ArticleService;
import com.inheaven.PetService.service.CommentService;
import com.inheaven.PetService.service.LikeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Đánh dấu đây là một Spring RESTful Controller
@RequestMapping("/api/articles") // Đường dẫn gốc cho tất cả API của controller này
@RequiredArgsConstructor // Tạo constructor có tham số cho các thuộc tính final
@CrossOrigin(origins = "*") // Cho phép truy cập từ các nguồn khác
public class ArticleController {

    private final ArticleService articleService; // Service xử lý logic nghiệp vụ cho bài viết
    private final CommentService commentService; // Service xử lý logic nghiệp vụ cho bình luận
    private final LikeService likeService; // Service xử lý logic nghiệp vụ cho lượt thích

    /**
     * API tạo bài viết mới
     *
     * @param articleRequestDto Thông tin bài viết cần tạo
     * @return Thông tin bài viết đã tạo
     */
    @PostMapping // Xử lý yêu cầu HTTP POST đến /api/articles
    public ResponseEntity<ArticleResponseDto> createArticle(@RequestBody ArticleRequestDto articleRequestDto) {
        // Gọi service để tạo bài viết mới
        ArticleResponseDto newArticle = articleService.createArticle(articleRequestDto);
        // Trả về kết quả với mã HTTP 201 (Created)
        return new ResponseEntity<>(newArticle, HttpStatus.CREATED);
    }

    /**
     * API lấy danh sách tất cả bài viết
     *
     * @return Danh sách bài viết
     */
    @GetMapping // Xử lý yêu cầu HTTP GET đến /api/articles
    public ResponseEntity<List<ArticleResponseDto>> getAllArticles() {
        // Gọi service để lấy danh sách tất cả bài viết
        List<ArticleResponseDto> articles = articleService.getAllArticles();
        // Trả về kết quả với mã HTTP 200 (OK)
        return ResponseEntity.ok(articles);
    }

    /**
     * API lấy thông tin bài viết theo ID
     *
     * @param articleId ID của bài viết
     * @return Thông tin bài viết
     */
    @GetMapping("/{articleId}") // Xử lý yêu cầu HTTP GET đến /api/articles/{articleId}
    public ResponseEntity<ArticleResponseDto> getArticleById(@PathVariable Long articleId) {
        // Gọi service để lấy thông tin bài viết theo ID
        ArticleResponseDto article = articleService.getArticleById(articleId);
        // Trả về kết quả với mã HTTP 200 (OK)
        return ResponseEntity.ok(article);
    }

    /**
     * API cập nhật thông tin bài viết
     *
     * @param articleId         ID của bài viết cần cập nhật
     * @param articleRequestDto Thông tin bài viết mới
     * @return Thông tin bài viết đã cập nhật
     */
    @PutMapping("/{articleId}") // Xử lý yêu cầu HTTP PUT đến /api/articles/{articleId}
    public ResponseEntity<ArticleResponseDto> updateArticle(@PathVariable Long articleId,
            @RequestBody ArticleRequestDto articleRequestDto) {
        // Gọi service để cập nhật thông tin bài viết
        ArticleResponseDto updatedArticle = articleService.updateArticle(articleId, articleRequestDto);
        // Trả về kết quả với mã HTTP 200 (OK)
        return ResponseEntity.ok(updatedArticle);
    }

    /**
     * API xóa bài viết
     *
     * @param articleId ID của bài viết cần xóa
     * @return Thông báo kết quả
     */
    @DeleteMapping("/{articleId}") // Xử lý yêu cầu HTTP DELETE đến /api/articles/{articleId}
    public ResponseEntity<MessageResponse> deleteArticle(@PathVariable Long articleId) {
        // Gọi service để xóa bài viết
        articleService.deleteArticle(articleId);
        // Trả về thông báo thành công với mã HTTP 200 (OK)
        return ResponseEntity.ok(
                MessageResponse.builder()
                        .message("Article deleted successfully")
                        .success(true)
                        .build());
    }

    /**
     * API lấy danh sách bài viết của một người dùng
     *
     * @param userId ID của người dùng
     * @return Danh sách bài viết
     */
    @GetMapping("/users/{userId}") // Xử lý yêu cầu HTTP GET đến /api/articles/users/{userId}
    public ResponseEntity<List<ArticleResponseDto>> getArticlesByUserId(@PathVariable Long userId) {
        // Gọi service để lấy danh sách bài viết của người dùng
        List<ArticleResponseDto> articles = articleService.getArticlesByUserId(userId);
        // Trả về kết quả với mã HTTP 200 (OK)
        return ResponseEntity.ok(articles);
    }

    /**
     * API lấy danh sách bài viết theo chủ đề
     *
     * @param subject Chủ đề
     * @return Danh sách bài viết
     */
    @GetMapping("/subjects/{subject}") // Xử lý yêu cầu HTTP GET đến /api/articles/subjects/{subject}
    public ResponseEntity<List<ArticleResponseDto>> getArticlesBySubject(@PathVariable String subject) {
        // Gọi service để lấy danh sách bài viết theo chủ đề
        List<ArticleResponseDto> articles = articleService.getArticlesBySubject(subject);
        // Trả về kết quả với mã HTTP 200 (OK)
        return ResponseEntity.ok(articles);
    }

    /**
     * API tìm kiếm bài viết đã được duyệt theo từ khóa
     *
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách bài viết
     */
    @GetMapping("/approved/search") // Xử lý yêu cầu HTTP GET đến /api/articles/approved/search?keyword=...
    public ResponseEntity<List<ArticleResponseDto>> searchApprovedArticles(@RequestParam String keyword) {
        // Gọi service để tìm kiếm bài viết đã được duyệt theo từ khóa
        List<ArticleResponseDto> articles = articleService.searchApprovedArticles(keyword);
        // Trả về kết quả với mã HTTP 200 (OK)
        return ResponseEntity.ok(articles);
    }

    /**
     * API lấy danh sách bài viết đã được duyệt
     *
     * @return Danh sách bài viết đã được duyệt
     */
    @GetMapping("/approved") // Xử lý yêu cầu HTTP GET đến /api/articles/approved
    public ResponseEntity<List<ArticleResponseDto>> getApprovedArticles() {
        // Gọi service để lấy danh sách bài viết đã được duyệt
        List<ArticleResponseDto> articles = articleService.getApprovedArticles();
        // Trả về kết quả với mã HTTP 200 (OK)
        return ResponseEntity.ok(articles);
    }

    /**
     * API lấy danh sách bài viết chưa được duyệt
     *
     * @return Danh sách bài viết chưa được duyệt
     */
    @GetMapping("/pending") // Xử lý yêu cầu HTTP GET đến /api/articles/pending
    public ResponseEntity<List<ArticleResponseDto>> getPendingArticles() {
        // Gọi service để lấy danh sách bài viết chưa được duyệt
        List<ArticleResponseDto> articles = articleService.getPendingArticles();
        // Trả về kết quả với mã HTTP 200 (OK)
        return ResponseEntity.ok(articles);
    }

    /**
     * API kiểm duyệt bài viết
     *
     * @param articleId ID của bài viết cần kiểm duyệt
     * @param status    Trạng thái kiểm duyệt (true: duyệt, false: không duyệt)
     * @return Thông tin bài viết đã cập nhật
     */
    @PutMapping("/{articleId}/moderation") // Xử lý yêu cầu HTTP PUT đến /api/articles/{articleId}/moderation
    public ResponseEntity<ArticleResponseDto> moderateArticle(@PathVariable Long articleId,
            @RequestParam boolean status) {
        // Gọi service để kiểm duyệt bài viết
        ArticleResponseDto updatedArticle = articleService.moderateArticle(articleId, status);
        // Trả về kết quả với mã HTTP 200 (OK)
        return ResponseEntity.ok(updatedArticle);
    }

    /**
     * API thêm bình luận cho bài viết
     *
     * @param articleId      ID của bài viết
     * @param userId         ID của người dùng
     * @param contentRequest Nội dung bình luận
     * @return Thông tin bình luận đã tạo
     */
    @PostMapping("/{articleId}/users/{userId}/comments")
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable Long articleId,
            @PathVariable Long userId,
            @RequestBody ContentRequest contentRequest) {

        // Tạo đối tượng CommentRequestDto từ thông tin path và body
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content(contentRequest.getContent())
                .userId(userId)
                .articleId(articleId)
                .build();

        // Gọi service để thêm bình luận
        CommentResponseDto newComment = commentService.createComment(commentRequestDto);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    /**
     * Lớp DTO đơn giản chỉ có trường nội dung bình luận
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContentRequest {
        private String content; // Nội dung bình luận
    }

    /**
     * API lấy danh sách bình luận của một bài viết
     *
     * @param articleId ID của bài viết
     * @return Danh sách bình luận
     */
    @GetMapping("/{articleId}/comments") // Xử lý yêu cầu HTTP GET đến /api/articles/{articleId}/comments
    public ResponseEntity<List<CommentResponseDto>> getCommentsByArticleId(@PathVariable Long articleId) {
        // Gọi service để lấy danh sách bình luận của bài viết
        List<CommentResponseDto> comments = commentService.getCommentsByArticleId(articleId);
        // Trả về kết quả với mã HTTP 200 (OK)
        return ResponseEntity.ok(comments);
    }

    /**
     * API thêm/xóa lượt thích cho bài viết
     */
    @PostMapping("/{articleId}/users/{userId}/likes")
    public ResponseEntity<?> toggleLike(
            @PathVariable Long articleId,
            @PathVariable Long userId) {

        // Tạo đối tượng LikeRequestDto từ thông tin path
        LikeRequestDto likeRequestDto = LikeRequestDto.builder()
                .userId(userId)
                .articleId(articleId)
                .build();

        // Gọi service để thêm/xóa lượt thích
        LikeResponseDto like = likeService.toggleLike(likeRequestDto);

        if (like != null) {
            return new ResponseEntity<>(like, HttpStatus.CREATED);
        } else {
            return ResponseEntity.ok(
                    MessageResponse.builder()
                            .message("Like removed successfully")
                            .success(true)
                            .build());
        }
    }

    /**
     * API lấy số lượng lượt thích của một bài viết
     *
     * @param articleId ID của bài viết
     * @return Số lượng lượt thích
     */
    @GetMapping("/{articleId}/likes/count") // Xử lý yêu cầu HTTP GET đến /api/articles/{articleId}/likes/count
    public ResponseEntity<Long> countLikesByArticleId(@PathVariable Long articleId) {
        // Gọi service để đếm số lượng lượt thích của bài viết
        long likeCount = likeService.countLikesByArticleId(articleId);
        // Trả về kết quả với mã HTTP 200 (OK)
        return ResponseEntity.ok(likeCount);
    }

    /**
     * API kiểm tra xem một người dùng đã thích một bài viết hay chưa
     *
     * @param articleId ID của bài viết
     * @param userId    ID của người dùng
     * @return true nếu đã thích, false nếu chưa thích
     */
    @GetMapping("/{articleId}/likes/users/{userId}") // Xử lý yêu cầu HTTP GET đến
                                                     // /api/articles/{articleId}/likes/users/{userId}
    public ResponseEntity<Boolean> hasUserLikedArticle(@PathVariable Long articleId,
            @PathVariable Long userId) {
        // Gọi service để kiểm tra xem người dùng đã thích bài viết hay chưa
        boolean hasLiked = likeService.hasUserLikedArticle(userId, articleId);
        // Trả về kết quả với mã HTTP 200 (OK)
        return ResponseEntity.ok(hasLiked);
    }
}