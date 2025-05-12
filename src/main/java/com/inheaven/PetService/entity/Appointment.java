package com.inheaven.PetService.entity;

import com.inheaven.PetService.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity // Đánh dấu đây là một entity (bảng trong DB)
@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class Appointment {
    @Id // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động sinh giá trị cho khóa chính
    private Long id; // ID của cuộc hẹn

    private String name; // Tên người đặt lịch
    private String petName; // Tên thú cưng
    private String type; // Loại thú cưng
    private String breed; // Giống thú cưng
    private String healthStatus; // Tình trạng sức khỏe hiện tại
    private String healthHistory; // Lịch sử sức khỏe
    private String note; // Ghi chú bổ sung

    @Enumerated(EnumType.STRING) // Lưu enum dạng STRING trong DB
    private AppointmentStatus status; // Trạng thái cuộc hẹn

    private LocalDateTime appointmentTime; // Thời gian hẹn
    private LocalDateTime checkInTime; // Thời gian check-in

    @ManyToOne(fetch = FetchType.LAZY) // Quan hệ nhiều-một với User, lazy loading
    @JoinColumn(name = "user_id") // Tên cột khóa ngoại
    private User user; // Người dùng đặt lịch

    @ManyToOne(fetch = FetchType.LAZY) // Quan hệ nhiều-một với User (bác sĩ)
    @JoinColumn(name = "preferred_doctor_id") // Tên cột khóa ngoại cho bác sĩ ưu tiên
    private User preferredDoctor; // Bác sĩ ưu tiên mà người dùng muốn khám

    @ManyToOne(fetch = FetchType.LAZY) // Quan hệ nhiều-một với User (bác sĩ thực hiện)
    @JoinColumn(name = "assigned_doctor_id") // Tên cột khóa ngoại cho bác sĩ thực hiện
    private User assignedDoctor; // Bác sĩ thực sự thực hiện khám (được ghi lại khi hoàn thành)
}
