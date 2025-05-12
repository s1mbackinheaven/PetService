package com.inheaven.PetService.dto;

import com.inheaven.PetService.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class AppointmentDTO {
    private Long id; // ID của cuộc hẹn
    private String name; // Tên người đặt lịch
    private String petName; // Tên thú cưng
    private String type; // Loại thú cưng
    private String breed; // Giống thú cưng
    private String healthStatus; // Tình trạng sức khỏe hiện tại
    private String healthHistory; // Lịch sử sức khỏe
    private String note; // Ghi chú bổ sung
    private AppointmentStatus status; // Trạng thái cuộc hẹn
    private LocalDateTime appointmentTime; // Thời gian hẹn
    private LocalDateTime checkInTime; // Thời gian check-in
    private Long userId; // ID của người dùng đặt lịch
    private Long preferredDoctorId; // ID của bác sĩ ưu tiên
    private String userName; // Tên người dùng đặt lịch
    private String preferredDoctorName; // Tên bác sĩ ưu tiên
    private Long assignedDoctorId; // ID của bác sĩ thực hiện khám
    private String assignedDoctorName; // Tên bác sĩ thực hiện khám
}