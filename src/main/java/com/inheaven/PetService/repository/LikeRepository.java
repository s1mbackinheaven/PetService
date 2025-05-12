package com.inheaven.PetService.repository;

import com.inheaven.PetService.entity.Article;
import com.inheaven.PetService.entity.Like;
import com.inheaven.PetService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Đánh dấu đây là một Spring Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    // Tìm tất cả lượt thích của một bài viết cụ thể
    List<Like> findByArticle(Article article);

    // Tìm tất cả lượt thích của một người dùng cụ thể
    List<Like> findByUser(User user);

    // Kiểm tra xem một người dùng đã thích một bài viết hay chưa
    Optional<Like> findByUserAndArticle(User user, Article article);

    // Đếm số lượng lượt thích cho một bài viết
    long countByArticle(Article article);

    // Xóa lượt thích của một người dùng cho một bài viết cụ thể
    void deleteByUserAndArticle(User user, Article article);

    // Kiểm tra xem một người dùng đã thích một bài viết hay chưa
    boolean existsByUserAndArticle(User user, Article article);
}