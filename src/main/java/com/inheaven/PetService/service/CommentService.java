package com.inheaven.PetService.service;

import com.inheaven.PetService.dto.CommentRequestDto;
import com.inheaven.PetService.dto.CommentResponseDto;
import com.inheaven.PetService.dto.UserDTO;
import com.inheaven.PetService.entity.Article;
import com.inheaven.PetService.entity.Comment;
import com.inheaven.PetService.entity.User;
import com.inheaven.PetService.exception.ResourceNotFoundException;
import com.inheaven.PetService.repository.ArticleRepository;
import com.inheaven.PetService.repository.CommentRepository;
import com.inheaven.PetService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service // Đánh dấu đây là một Spring Service
@RequiredArgsConstructor // Tạo constructor có tham số cho các thuộc tính final
public class CommentService {

    private final CommentRepository commentRepository; // Repository để thao tác với bảng Comment
    private final UserRepository userRepository; // Repository để thao tác với bảng User
    private final ArticleRepository articleRepository; // Repository để thao tác với bảng Article

    /**
     * Tạo một bình luận mới
     *
     * @param commentRequestDto Thông tin bình luận cần tạo
     * @return Thông tin bình luận đã tạo
     */
    @Transactional // Đảm bảo tính toàn vẹn của giao dịch
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto) {
        // Tìm người dùng theo ID
        User user = userRepository.findById(commentRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + commentRequestDto.getUserId()));

        // Tìm bài viết theo ID
        Article article = articleRepository.findById(commentRequestDto.getArticleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Article not found with id: " + commentRequestDto.getArticleId()));

        // Tạo đối tượng Comment từ CommentRequestDto
        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent())
                .user(user)
                .article(article)
                .build();

        // Lưu bình luận vào cơ sở dữ liệu
        Comment savedComment = commentRepository.save(comment);

        // Chuyển đổi từ entity sang DTO và trả về
        return convertToCommentResponseDto(savedComment);
    }

    /**
     * Lấy thông tin của một bình luận theo ID
     *
     * @param commentId ID của bình luận cần lấy thông tin
     * @return Thông tin bình luận
     */
    public CommentResponseDto getCommentById(Long commentId) {
        // Tìm bình luận theo ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        // Chuyển đổi từ entity sang DTO và trả về
        return convertToCommentResponseDto(comment);
    }

    /**
     * Lấy danh sách bình luận của một bài viết
     *
     * @param articleId ID của bài viết
     * @return Danh sách bình luận
     */
    public List<CommentResponseDto> getCommentsByArticleId(Long articleId) {
        // Tìm bài viết theo ID
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with id: " + articleId));

        // Lấy danh sách bình luận của bài viết, sắp xếp theo thời gian tạo giảm dần
        // (mới nhất đầu tiên)
        List<Comment> comments = commentRepository.findByArticleOrderByCreationDateDesc(article);

        // Chuyển đổi danh sách từ entity sang DTO và trả về
        return comments.stream()
                .map(this::convertToCommentResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Cập nhật nội dung của một bình luận
     *
     * @param commentId         ID của bình luận cần cập nhật
     * @param commentRequestDto Thông tin bình luận mới
     * @return Thông tin bình luận đã cập nhật
     */
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto) {
        // Tìm bình luận theo ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        // Cập nhật nội dung bình luận
        comment.setContent(commentRequestDto.getContent());

        // Lưu bình luận đã cập nhật vào cơ sở dữ liệu
        Comment updatedComment = commentRepository.save(comment);

        // Chuyển đổi từ entity sang DTO và trả về
        return convertToCommentResponseDto(updatedComment);
    }

    /**
     * Xóa một bình luận
     *
     * @param commentId ID của bình luận cần xóa
     */
    @Transactional
    public void deleteComment(Long commentId) {
        // Tìm bình luận theo ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        // Xóa bình luận
        commentRepository.delete(comment);
    }

    /**
     * Chuyển đổi từ entity Comment sang DTO CommentResponseDto
     *
     * @param comment Entity Comment
     * @return DTO CommentResponseDto
     */
    private CommentResponseDto convertToCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .creationDate(comment.getCreationDate())
                .user(mapUserToDto(comment.getUser()))
                .articleId(comment.getArticle().getId())
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