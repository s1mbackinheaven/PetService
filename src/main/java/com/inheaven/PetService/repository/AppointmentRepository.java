package com.inheaven.PetService.repository;

import com.inheaven.PetService.entity.Appointment;
import com.inheaven.PetService.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Đánh dấu đây là repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Tìm tất cả lịch hẹn theo ID người dùng
    List<Appointment> findByUserId(Long userId);

    // Tìm tất cả lịch hẹn theo ID bác sĩ ưu tiên
    List<Appointment> findByPreferredDoctorId(Long preferredDoctorId);

    // Tìm tất cả lịch hẹn theo trạng thái
    List<Appointment> findByStatus(AppointmentStatus status);

    // Tìm tất cả lịch hẹn đã check-in theo thứ tự thời gian check-in (FIFO)
    @Query("SELECT a FROM Appointment a WHERE a.status = :status ORDER BY a.checkInTime ASC")
    List<Appointment> findByStatusOrderByCheckInTimeAsc(AppointmentStatus status);

    // Tìm lịch hẹn đã check-in và có bác sĩ ưu tiên
    @Query("SELECT a FROM Appointment a WHERE a.status = :status AND a.preferredDoctor.id = :doctorId ORDER BY a.checkInTime ASC")
    List<Appointment> findByStatusAndPreferredDoctorIdOrderByCheckInTimeAsc(AppointmentStatus status, Long doctorId);

    // Tìm lịch hẹn theo tên người đặt (tìm kiếm không phân biệt hoa thường)
    @Query("SELECT a FROM Appointment a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY a.appointmentTime DESC")
    List<Appointment> findByNameContainingIgnoreCase(String name);

    // Tìm lịch hẹn theo tên người đặt và trạng thái
    @Query("SELECT a FROM Appointment a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%')) AND a.status = :status ORDER BY a.appointmentTime DESC")
    List<Appointment> findByNameContainingIgnoreCaseAndStatus(String name, AppointmentStatus status);
}