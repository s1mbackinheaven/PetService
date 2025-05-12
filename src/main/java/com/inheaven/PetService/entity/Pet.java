package com.inheaven.PetService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Đánh dấu đây là một entity (bảng trong DB)
@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class Pet {

    @Id // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động sinh giá trị cho khóa chính
    private Long id;
    private String nickName;
    private String name;
    private String type;
    private String breed;
    private String gender;
    private double weight;
    private String birthday;
    private String avatarPet;
    private String description;
    private String healthStatus;
    private String healthHistory;

    @Column(columnDefinition = "TEXT") // Định nghĩa kiểu dữ liệu là TEXT để lưu được nội dung dài
    private String suggestion;

    @ManyToOne(fetch = FetchType.LAZY) // Quan hệ nhiều-một với User, lazy loading
    @JoinColumn(name = "user_id") // Tên cột khóa ngoại
    private User user; // User sở hữu pet này
}
