package com.inheaven.PetService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class ArticleRequestDto {

    private String subject; // Chủ đề bài viết
    private String title; // Tiêu đề bài viết
    private String content; // Nội dung bài viết
    private Long userId; // ID của người dùng tạo bài viết
}