package com.inheaven.PetService.controller;

import com.inheaven.PetService.dto.AppointmentDTO;
import com.inheaven.PetService.dto.AppointmentRequest;
import com.inheaven.PetService.dto.UpdateNoteRequest;
import com.inheaven.PetService.enums.AppointmentStatus;
import com.inheaven.PetService.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Đánh dấu đây là controller REST
@RequestMapping("/api/appointments") // Đường dẫn cơ sở cho tất cả API trong controller này
@RequiredArgsConstructor // Tự động tạo constructor cho các trường final
public class AppointmentController {

    private final AppointmentService appointmentService; // Service xử lý lịch hẹn

    /**
     * Tạo một lịch hẹn mới
     * 
     * @param userId  ID của người dùng đặt lịch
     * @param request Thông tin lịch hẹn
     * @return Lịch hẹn đã được tạo
     */
    @PostMapping("/user/{userId}")
    public ResponseEntity<AppointmentDTO> createAppointment(
            @PathVariable Long userId,
            @Valid @RequestBody AppointmentRequest request) {
        // Tạo lịch hẹn mới và trả về
        return new ResponseEntity<>(appointmentService.createAppointment(request, userId), HttpStatus.CREATED);
    }

    /**
     * Cập nhật một lịch hẹn
     * 
     * @param id      ID của lịch hẹn cần cập nhật
     * @param request Thông tin lịch hẹn mới
     * @return Lịch hẹn đã được cập nhật
     */
    @PutMapping("/{id}") // PUT /api/appointments/{id}
    public ResponseEntity<AppointmentDTO> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequest request) {
        // Cập nhật lịch hẹn và trả về
        return ResponseEntity.ok(appointmentService.updateAppointment(id, request));
    }

    /**
     * Lấy thông tin một lịch hẹn theo ID
     * 
     * @param id ID của lịch hẹn
     * @return Thông tin lịch hẹn
     */
    @GetMapping("/{id}") // GET /api/appointments/{id}
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        // Lấy thông tin lịch hẹn và trả về
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    /**
     * Lấy tất cả các lịch hẹn
     * 
     * @return Danh sách tất cả lịch hẹn
     */
    @GetMapping // GET /api/appointments
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        // Lấy tất cả lịch hẹn và trả về
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    /**
     * Lấy tất cả lịch hẹn của một người dùng
     * 
     * @param userId ID của người dùng
     * @return Danh sách lịch hẹn của người dùng
     */
    @GetMapping("/user/{userId}") // GET /api/appointments/user/{userId}
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByUserId(@PathVariable Long userId) {
        // Lấy tất cả lịch hẹn của người dùng và trả về
        return ResponseEntity.ok(appointmentService.getAppointmentsByUserId(userId));
    }

    /**
     * Check-in một lịch hẹn
     * 
     * @param id ID của lịch hẹn
     * @return Thông tin lịch hẹn sau khi đã check-in
     */
    @PostMapping("/{id}/check-in") // POST /api/appointments/{id}/check-in
    public ResponseEntity<AppointmentDTO> checkInAppointment(@PathVariable Long id) {
        // Check-in lịch hẹn và trả về
        return ResponseEntity.ok(appointmentService.checkInAppointment(id));
    }

    /**
     * Hoàn thành một lịch hẹn
     * 
     * @param id       ID của lịch hẹn
     * @param doctorId ID của bác sĩ thực hiện khám
     * @return Thông tin lịch hẹn sau khi đã hoàn thành
     */
    @PostMapping("/{id}/complete")
    public ResponseEntity<AppointmentDTO> completeAppointment(
            @PathVariable Long id,
            @RequestParam Long doctorId) {
        // Hoàn thành lịch hẹn và trả về
        return ResponseEntity.ok(appointmentService.completeAppointment(id, doctorId));
    }

    /**
     * Lấy lịch hẹn tiếp theo cho bác sĩ
     * 
     * @param doctorId ID của bác sĩ
     * @return Thông tin lịch hẹn tiếp theo
     */
    @GetMapping("/next-for-doctor/{doctorId}") // GET /api/appointments/next-for-doctor/{doctorId}
    public ResponseEntity<AppointmentDTO> getNextAppointmentForDoctor(@PathVariable Long doctorId) {
        // Lấy lịch hẹn tiếp theo cho bác sĩ và trả về
        return ResponseEntity.ok(appointmentService.getNextAppointmentForDoctor(doctorId));
    }

    /**
     * Xóa một lịch hẹn
     * 
     * @param id ID của lịch hẹn cần xóa
     * @return Thông báo xóa thành công
     */
    @DeleteMapping("/{id}") // DELETE /api/appointments/{id}
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        // Xóa lịch hẹn
        appointmentService.deleteAppointment(id);
        // Trả về NO_CONTENT (204)
        return ResponseEntity.noContent().build();
    }

    /**
     * Trả lịch hẹn về lại hàng đợi
     * 
     * @param id ID của lịch hẹn
     * @return Thông tin lịch hẹn sau khi đã trả về hàng đợi
     */
    @PostMapping("/{id}/return-to-queue")
    public ResponseEntity<AppointmentDTO> returnToQueue(@PathVariable Long id) {
        // Trả lịch hẹn về hàng đợi và trả về
        return ResponseEntity.ok(appointmentService.returnToQueue(id));
    }

    /**
     * Tìm kiếm lịch hẹn theo tên người đặt lịch
     *
     * @param name Tên người đặt lịch cần tìm
     * @return Danh sách lịch hẹn phù hợp
     */
    @GetMapping("/search")
    public ResponseEntity<List<AppointmentDTO>> searchAppointmentsByName(@RequestParam String name) {
        // Tìm kiếm lịch hẹn theo tên và trả về
        return ResponseEntity.ok(appointmentService.searchAppointmentsByName(name));
    }

    /**
     * Tìm kiếm lịch hẹn theo tên người đặt lịch và trạng thái
     *
     * @param name   Tên người đặt lịch cần tìm
     * @param status Trạng thái lịch hẹn cần lọc
     * @return Danh sách lịch hẹn phù hợp
     */
    @GetMapping("/search-with-status")
    public ResponseEntity<List<AppointmentDTO>> searchAppointmentsByNameAndStatus(
            @RequestParam String name,
            @RequestParam(required = false) AppointmentStatus status) {
        // Tìm kiếm lịch hẹn theo tên và trạng thái, sau đó trả về
        return ResponseEntity.ok(appointmentService.searchAppointmentsByNameAndStatus(name, status));
    }

    /**
     * Cập nhật ghi chú của lịch hẹn đang khám
     * 
     * @param id      ID của lịch hẹn
     * @param request Yêu cầu cập nhật ghi chú
     * @return Thông tin lịch hẹn sau khi cập nhật
     */
    @PutMapping("/{id}/update-note")
    public ResponseEntity<AppointmentDTO> updateAppointmentNote(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNoteRequest request) {
        // Cập nhật ghi chú lịch hẹn và trả về
        return ResponseEntity.ok(appointmentService.updateAppointmentNote(id, request.getNote()));
    }
}