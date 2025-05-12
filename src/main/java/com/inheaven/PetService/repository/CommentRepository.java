package com.inheaven.PetService.repository;

import com.inheaven.PetService.entity.Article;
import com.inheaven.PetService.entity.Comment;
import com.inheaven.PetService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Đánh dấu đây là một Spring Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Tìm tất cả bình luận của một bài viết cụ thể
    List<Comment> findByArticle(Article article);

    // Tìm tất cả bình luận của một người dùng cụ thể
    List<Comment> findByUser(User user);

    // Tìm tất cả bình luận của một bài viết, sắp xếp theo thời gian tạo giảm dần
    // (mới nhất đầu tiên)
    List<Comment> findByArticleOrderByCreationDateDesc(Article article);

    // Tìm tất cả bình luận của một người dùng cho một bài viết cụ thể
    List<Comment> findByUserAndArticle(User user, Article article);

    // Đếm số lượng bình luận cho một bài viết
    long countByArticle(Article article);
}