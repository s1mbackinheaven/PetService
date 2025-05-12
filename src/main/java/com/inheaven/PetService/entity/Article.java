package com.inheaven.PetService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity // Đánh dấu đây là một entity (bảng trong DB)
@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
@Table(name = "articles") // Tên bảng trong cơ sở dữ liệu
public class Article {

    @Id // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động sinh giá trị cho khóa chính
    private Long id; // ID của bài viết

    @Column(nullable = false) // Trường này không được phép null
    private String subject; // Chủ đề bài viết

    @Column(nullable = false) // Trường này không được phép null
    private String title; // Tiêu đề bài viết

    @Temporal(TemporalType.TIMESTAMP) // Định dạng thời gian
    private Date creationDate; // Ngày tạo bài viết

    @Lob // Large Object - cho phép lưu trữ dữ liệu lớn
    @Column(columnDefinition = "LONGTEXT") // Sử dụng kiểu LONGTEXT để lưu trữ nội dung lớn
    private String content; // Nội dung bài viết

    @ManyToOne(fetch = FetchType.LAZY) // Quan hệ nhiều-một với User, lazy loading để tối ưu hiệu suất
    @JoinColumn(name = "user_id", nullable = false) // Khóa ngoại liên kết với User
    private User user; // Người dùng tạo bài viết

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true) // Quan hệ một-nhiều với Comment
    private List<Comment> comments = new ArrayList<>(); // Danh sách comment của bài viết

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true) // Quan hệ một-nhiều với Like
    private List<Like> likes = new ArrayList<>(); // Danh sách like của bài viết

    @Column(nullable = false) // Trường này không được phép null
    private Boolean active = false; // Trạng thái kiểm duyệt của bài viết, mặc định là false (chưa duyệt)

    @PrePersist // Phương thức này sẽ được gọi trước khi entity được lưu vào DB
    public void prePersist() {
        // Gán thời gian hiện tại cho trường creationDate trước khi lưu
        this.creationDate = new Date();
    }
}