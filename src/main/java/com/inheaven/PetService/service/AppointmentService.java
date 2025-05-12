package com.inheaven.PetService.service;

import com.inheaven.PetService.dto.AppointmentDTO;
import com.inheaven.PetService.dto.AppointmentRequest;
import com.inheaven.PetService.enums.AppointmentStatus;

import java.util.List;

public interface AppointmentService {

    // Tạo lịch hẹn mới
    AppointmentDTO createAppointment(AppointmentRequest request, Long userId);

    // Cập nhật lịch hẹn
    AppointmentDTO updateAppointment(Long id, AppointmentRequest request);

    // Cập nhật note của lịch hẹn khi đang khám
    AppointmentDTO updateAppointmentNote(Long id, String note);

    // Tìm lịch hẹn theo ID
    AppointmentDTO getAppointmentById(Long id);

    // Lấy tất cả lịch hẹn
    List<AppointmentDTO> getAllAppointments();

    // Lấy lịch hẹn theo ID người dùng
    List<AppointmentDTO> getAppointmentsByUserId(Long userId);

    // Tìm kiếm lịch hẹn theo tên người đặt
    List<AppointmentDTO> searchAppointmentsByName(String name);

    // Tìm kiếm lịch hẹn theo tên người đặt và trạng thái
    List<AppointmentDTO> searchAppointmentsByNameAndStatus(String name, AppointmentStatus status);

    // Check-in lịch hẹn
    AppointmentDTO checkInAppointment(Long id);

    // Hoàn thành lịch hẹn và gán bác sĩ thực hiện
    AppointmentDTO completeAppointment(Long id, Long doctorId);

    // Lấy lịch hẹn tiếp theo từ hàng đợi cho bác sĩ
    AppointmentDTO getNextAppointmentForDoctor(Long doctorId);

    // Trả lịch hẹn về lại hàng đợi
    AppointmentDTO returnToQueue(Long id);

    // Hủy lịch hẹn
    void deleteAppointment(Long id);
}