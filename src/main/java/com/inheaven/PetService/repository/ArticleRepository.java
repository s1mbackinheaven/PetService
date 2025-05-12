package com.inheaven.PetService.repository;

import com.inheaven.PetService.entity.Article;
import com.inheaven.PetService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Đánh dấu đây là một Spring Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // Tìm các bài viết của một người dùng cụ thể
    List<Article> findByUser(User user);

    // Tìm các bài viết theo chủ đề
    List<Article> findBySubject(String subject);

    // Tìm các bài viết chứa từ khóa trong tiêu đề
    List<Article> findByTitleContaining(String keyword);

    // Tìm các bài viết chứa từ khóa trong nội dung
    List<Article> findByContentContaining(String keyword);

    // Tìm các bài viết chứa từ khóa trong tiêu đề hoặc nội dung
    List<Article> findByTitleContainingOrContentContaining(String title, String content);

    // Tìm các bài viết đã được duyệt (active = true)
    List<Article> findByActiveTrue();

    // Tìm các bài viết chưa được duyệt (active = false)
    List<Article> findByActiveFalse();

    // Tìm các bài viết đã duyệt và chứa từ khóa trong tiêu đề hoặc nội dung
    List<Article> findByTitleContainingOrContentContainingAndActiveTrue(String title, String content);
}