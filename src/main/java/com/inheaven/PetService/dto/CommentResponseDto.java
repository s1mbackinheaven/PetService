package com.inheaven.PetService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class CommentResponseDto {

    private Long id; // ID của bình luận
    private String content; // Nội dung bình luận
    private Date creationDate; // Ngày tạo bình luận
    private UserDTO user; // Thông tin người dùng tạo bình luận
    private Long articleId; // ID của bài viết được bình luận
}