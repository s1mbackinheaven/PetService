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
public class User {

    @Id // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động sinh giá trị cho khóa chính
    private Long id;
    private String username; // Tên người dùng
    private String fullname;
    private String gender;
    private String email;
    private String phone;
    private String role;
    private String address;
    private String avatar;
    private String password; // Mật khẩu của người dùng (sẽ mã hóa)
    private String status;

}
