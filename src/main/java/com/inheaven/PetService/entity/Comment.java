package com.inheaven.PetService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity // Đánh dấu đây là một entity (bảng trong DB)
@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
@Table(name = "comments") // Tên bảng trong cơ sở dữ liệu
public class Comment {

    @Id // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động sinh giá trị cho khóa chính
    private Long id; // ID của bình luận

    @Lob // Large Object - cho phép lưu trữ dữ liệu lớn
    @Column(nullable = false) // Trường này không được phép null
    private String content; // Nội dung bình luận

    @Temporal(TemporalType.TIMESTAMP) // Định dạng thời gian
    private Date creationDate; // Ngày tạo bình luận

    @ManyToOne(fetch = FetchType.LAZY) // Quan hệ nhiều-một với User, lazy loading để tối ưu hiệu suất
    @JoinColumn(name = "user_id", nullable = false) // Khóa ngoại liên kết với User
    private User user; // Người dùng tạo bình luận

    @ManyToOne(fetch = FetchType.LAZY) // Quan hệ nhiều-một với Article, lazy loading để tối ưu hiệu suất
    @JoinColumn(name = "article_id", nullable = false) // Khóa ngoại liên kết với Article
    private Article article; // Bài viết được bình luận

    @PrePersist // Phương thức này sẽ được gọi trước khi entity được lưu vào DB
    public void prePersist() {
        // Gán thời gian hiện tại cho trường creationDate trước khi lưu
        this.creationDate = new Date();
    }
}