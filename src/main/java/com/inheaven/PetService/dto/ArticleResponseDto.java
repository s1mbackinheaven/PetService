package com.inheaven.PetService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class ArticleResponseDto {

    private Long id; // ID của bài viết
    private String subject; // Chủ đề bài viết
    private String title; // Tiêu đề bài viết
    private Date creationDate; // Ngày tạo bài viết
    private String content; // Nội dung bài viết
    private UserDTO user; // Thông tin người dùng tạo bài viết
    private List<CommentResponseDto> comments; // Danh sách bình luận
    private int likeCount; // Số lượng lượt thích
    private List<LikeResponseDto> likes; // Danh sách người dùng đã thích
    private Boolean active; // Trạng thái kiểm duyệt của bài viết
}