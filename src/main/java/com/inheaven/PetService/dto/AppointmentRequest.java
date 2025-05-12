package com.inheaven.PetService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class AppointmentRequest {
    @NotBlank(message = "Tên không được để trống") // Kiểm tra tên không được trống
    private String name; // Tên người đặt lịch

    @NotBlank(message = "Tên thú cưng không được để trống") // Kiểm tra tên thú cưng không được trống
    private String petName; // Tên thú cưng

    @NotBlank(message = "Loại thú cưng không được để trống") // Kiểm tra loại thú cưng không được trống
    private String type; // Loại thú cưng

    private String breed; // Giống thú cưng

    @NotBlank(message = "Tình trạng sức khỏe không được để trống") // Kiểm tra tình trạng sức khỏe không được trống
    private String healthStatus; // Tình trạng sức khỏe hiện tại

    private String healthHistory; // Lịch sử sức khỏe
    private String note; // Ghi chú bổ sung

    @NotNull(message = "Thời gian hẹn không được để trống") // Kiểm tra thời gian hẹn không được null
    private LocalDateTime appointmentTime; // Thời gian hẹn

    private Long preferredDoctorId; // ID của bác sĩ ưu tiên (có thể null)
}