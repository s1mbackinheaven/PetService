package com.inheaven.PetService.service.impl;

import com.inheaven.PetService.dto.AppointmentDTO;
import com.inheaven.PetService.dto.AppointmentRequest;
import com.inheaven.PetService.entity.Appointment;
import com.inheaven.PetService.entity.User;
import com.inheaven.PetService.enums.AppointmentStatus;
import com.inheaven.PetService.exception.AppointmentNotFoundException;
import com.inheaven.PetService.exception.EmptyQueueException;
import com.inheaven.PetService.exception.InvalidAppointmentStatusException;
import com.inheaven.PetService.repository.AppointmentRepository;
import com.inheaven.PetService.repository.UserRepository;
import com.inheaven.PetService.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service // Đánh dấu đây là một service
@RequiredArgsConstructor // Tự động tạo constructor cho các trường final
@Transactional // Đảm bảo tính nguyên vẹn giao dịch
public class AppointmentServiceImpl implements AppointmentService {

        private final AppointmentRepository appointmentRepository; // Repository xử lý lịch hẹn
        private final UserRepository userRepository; // Repository xử lý người dùng

        @Override
        public AppointmentDTO createAppointment(AppointmentRequest request, Long userId) {
                // Tìm User từ userId
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

                // Tìm bác sĩ ưu tiên (nếu có)
                User preferredDoctor = null;
                if (request.getPreferredDoctorId() != null) {
                        preferredDoctor = userRepository.findById(request.getPreferredDoctorId())
                                        .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ với ID: "
                                                        + request.getPreferredDoctorId()));
                }

                // Tạo đối tượng Appointment
                Appointment appointment = Appointment.builder()
                                .name(request.getName())
                                .petName(request.getPetName())
                                .type(request.getType())
                                .breed(request.getBreed())
                                .healthStatus(request.getHealthStatus())
                                .healthHistory(request.getHealthHistory())
                                .note(request.getNote())
                                .status(AppointmentStatus.SCHEDULED) // Mặc định là trạng thái đã đặt lịch
                                .appointmentTime(request.getAppointmentTime())
                                .user(user)
                                .preferredDoctor(preferredDoctor)
                                .build();

                // Lưu vào CSDL
                appointment = appointmentRepository.save(appointment);

                // Chuyển sang DTO và trả về
                return convertToDTO(appointment);
        }

        @Override
        public AppointmentDTO updateAppointment(Long id, AppointmentRequest request) {
                // Tìm lịch hẹn cần cập nhật
                Appointment appointment = appointmentRepository.findById(id)
                                .orElseThrow(() -> new AppointmentNotFoundException(id));

                // Kiểm tra trạng thái, chỉ cho phép cập nhật nếu đang ở trạng thái SCHEDULED
                if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
                        throw new InvalidAppointmentStatusException(
                                        "Không thể cập nhật lịch hẹn ở trạng thái " + appointment.getStatus());
                }

                // Giữ nguyên người dùng (không cập nhật userId)
                User user = appointment.getUser();

                // Tìm bác sĩ ưu tiên (nếu có)
                User preferredDoctor = null;
                if (request.getPreferredDoctorId() != null) {
                        preferredDoctor = userRepository.findById(request.getPreferredDoctorId())
                                        .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ với ID: "
                                                        + request.getPreferredDoctorId()));
                }

                // Cập nhật thông tin
                appointment.setName(request.getName());
                appointment.setPetName(request.getPetName());
                appointment.setType(request.getType());
                appointment.setBreed(request.getBreed());
                appointment.setHealthStatus(request.getHealthStatus());
                appointment.setHealthHistory(request.getHealthHistory());
                appointment.setNote(request.getNote());
                appointment.setAppointmentTime(request.getAppointmentTime());
                appointment.setUser(user);
                appointment.setPreferredDoctor(preferredDoctor);

                // Lưu vào CSDL
                appointment = appointmentRepository.save(appointment);

                // Chuyển sang DTO và trả về
                return convertToDTO(appointment);
        }

        @Override
        public AppointmentDTO getAppointmentById(Long id) {
                // Tìm lịch hẹn theo ID
                Appointment appointment = appointmentRepository.findById(id)
                                .orElseThrow(() -> new AppointmentNotFoundException(id));

                // Chuyển sang DTO và trả về
                return convertToDTO(appointment);
        }

        @Override
        public List<AppointmentDTO> getAllAppointments() {
                // Lấy tất cả lịch hẹn
                List<Appointment> appointments = appointmentRepository.findAll();

                // Chuyển sang DTO và trả về
                return appointments.stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public List<AppointmentDTO> getAppointmentsByUserId(Long userId) {
                // Tìm người dùng
                userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

                // Lấy tất cả lịch hẹn của người dùng
                List<Appointment> appointments = appointmentRepository.findByUserId(userId);

                // Chuyển sang DTO và trả về
                return appointments.stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public AppointmentDTO checkInAppointment(Long id) {
                // Tìm lịch hẹn theo ID
                Appointment appointment = appointmentRepository.findById(id)
                                .orElseThrow(() -> new AppointmentNotFoundException(id));

                // Kiểm tra trạng thái, chỉ cho phép check-in nếu đang ở trạng thái SCHEDULED
                if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
                        throw new InvalidAppointmentStatusException(
                                        "Không thể check-in lịch hẹn ở trạng thái " + appointment.getStatus());
                }

                // Cập nhật trạng thái thành CHECKED_IN và thời gian check-in
                appointment.setStatus(AppointmentStatus.CHECKED_IN);
                appointment.setCheckInTime(LocalDateTime.now());

                // Lưu vào CSDL
                appointment = appointmentRepository.save(appointment);

                // Chuyển sang DTO và trả về
                return convertToDTO(appointment);
        }

        @Override
        public AppointmentDTO completeAppointment(Long id, Long doctorId) {
                // Tìm lịch hẹn theo ID
                Appointment appointment = appointmentRepository.findById(id)
                                .orElseThrow(() -> new AppointmentNotFoundException(id));

                // Kiểm tra trạng thái, chỉ cho phép hoàn thành nếu đang ở trạng thái
                // IN_PROGRESS
                if (appointment.getStatus() != AppointmentStatus.IN_PROGRESS) {
                        throw new InvalidAppointmentStatusException(
                                        "Không thể hoàn thành lịch hẹn ở trạng thái " + appointment.getStatus() +
                                                        ". Lịch hẹn phải đang được khám (IN_PROGRESS)");
                }

                // Tìm bác sĩ thực hiện
                User doctor = userRepository.findById(doctorId)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ với ID: " + doctorId));

                // Kiểm tra nếu người dùng có vai trò là bác sĩ
                if (!"DOCTOR".equals(doctor.getRole())) {
                        throw new RuntimeException("Người dùng với ID: " + doctorId + " không phải là bác sĩ");
                }

                // Cập nhật trạng thái thành COMPLETED và lưu thông tin bác sĩ thực hiện
                appointment.setStatus(AppointmentStatus.COMPLETED);
                appointment.setAssignedDoctor(doctor);

                // Lưu vào CSDL
                appointment = appointmentRepository.save(appointment);

                // Chuyển sang DTO và trả về
                return convertToDTO(appointment);
        }

        @Override
        public AppointmentDTO getNextAppointmentForDoctor(Long doctorId) {
                // Tìm bác sĩ
                User doctor = userRepository.findById(doctorId)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ với ID: " + doctorId));

                // Lấy tất cả lịch hẹn có trạng thái CHECKED_IN
                List<Appointment> allCheckedInAppointments = appointmentRepository
                                .findByStatusOrderByCheckInTimeAsc(AppointmentStatus.CHECKED_IN);

                if (allCheckedInAppointments.isEmpty()) {
                        throw new EmptyQueueException();
                }

                // Tìm lịch hẹn phù hợp:
                // 1. Không có bác sĩ ưu tiên, hoặc
                // 2. Có bác sĩ ưu tiên là bác sĩ hiện tại
                List<Appointment> eligibleAppointments = allCheckedInAppointments.stream()
                                .filter(appointment -> appointment.getPreferredDoctor() == null ||
                                                appointment.getPreferredDoctor().getId().equals(doctorId))
                                .collect(Collectors.toList());

                if (eligibleAppointments.isEmpty()) {
                        throw new EmptyQueueException(
                                        "Không có lịch hẹn nào phù hợp cho bác sĩ. Tất cả lịch hẹn đều chỉ định bác sĩ khác.");
                }

                // Lấy lịch hẹn đầu tiên (đã sắp xếp theo thời gian check-in)
                Appointment nextAppointment = eligibleAppointments.get(0);

                // Cập nhật trạng thái lịch hẹn thành IN_PROGRESS và lưu thông tin bác sĩ
                nextAppointment.setStatus(AppointmentStatus.IN_PROGRESS);
                nextAppointment.setAssignedDoctor(doctor); // Gán bác sĩ tạm thời (có thể thay đổi khi hoàn thành)

                // Lưu vào CSDL
                nextAppointment = appointmentRepository.save(nextAppointment);

                // Chuyển sang DTO và trả về
                return convertToDTO(nextAppointment);
        }

        @Override
        public void deleteAppointment(Long id) {
                // Tìm lịch hẹn theo ID
                Appointment appointment = appointmentRepository.findById(id)
                                .orElseThrow(() -> new AppointmentNotFoundException(id));

                // Kiểm tra trạng thái, chỉ cho phép xóa nếu đang ở trạng thái SCHEDULED
                if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
                        throw new InvalidAppointmentStatusException(
                                        "Không thể xóa lịch hẹn ở trạng thái " + appointment.getStatus());
                }

                // Xóa lịch hẹn khỏi CSDL
                appointmentRepository.delete(appointment);
        }

        @Override
        public AppointmentDTO returnToQueue(Long id) {
                // Tìm lịch hẹn theo ID
                Appointment appointment = appointmentRepository.findById(id)
                                .orElseThrow(() -> new AppointmentNotFoundException(id));

                // Kiểm tra trạng thái, chỉ cho phép trả về hàng đợi nếu đang ở trạng thái
                // IN_PROGRESS
                if (appointment.getStatus() != AppointmentStatus.IN_PROGRESS) {
                        throw new InvalidAppointmentStatusException(
                                        "Không thể trả lịch hẹn về hàng đợi khi đang ở trạng thái "
                                                        + appointment.getStatus());
                }

                // Cập nhật trạng thái thành CHECKED_IN
                appointment.setStatus(AppointmentStatus.CHECKED_IN);

                // Xóa bác sĩ đã gán tạm thời
                appointment.setAssignedDoctor(null);

                // Cập nhật thời gian check-in để đảm bảo nằm cuối hàng đợi
                appointment.setCheckInTime(LocalDateTime.now());

                // Lưu vào CSDL
                appointment = appointmentRepository.save(appointment);

                // Chuyển sang DTO và trả về
                return convertToDTO(appointment);
        }

        @Override
        public List<AppointmentDTO> searchAppointmentsByName(String name) {
                if (name == null || name.trim().isEmpty()) {
                        throw new IllegalArgumentException("Tên tìm kiếm không được để trống");
                }

                // Lấy tất cả lịch hẹn có tên chứa chuỗi tìm kiếm
                List<Appointment> appointments = appointmentRepository.findByNameContainingIgnoreCase(name.trim());

                // Chuyển sang DTO và trả về
                return appointments.stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public List<AppointmentDTO> searchAppointmentsByNameAndStatus(String name, AppointmentStatus status) {
                if (name == null || name.trim().isEmpty()) {
                        throw new IllegalArgumentException("Tên tìm kiếm không được để trống");
                }

                if (status == null) {
                        return searchAppointmentsByName(name);
                }

                // Lấy tất cả lịch hẹn có tên chứa chuỗi tìm kiếm và có trạng thái tương ứng
                List<Appointment> appointments = appointmentRepository
                                .findByNameContainingIgnoreCaseAndStatus(name.trim(), status);

                // Chuyển sang DTO và trả về
                return appointments.stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public AppointmentDTO updateAppointmentNote(Long id, String note) {
                // Tìm lịch hẹn theo ID
                Appointment appointment = appointmentRepository.findById(id)
                                .orElseThrow(() -> new AppointmentNotFoundException(id));

                // Kiểm tra trạng thái, chỉ cho phép cập nhật note nếu đang ở trạng thái
                // IN_PROGRESS
                if (appointment.getStatus() != AppointmentStatus.IN_PROGRESS) {
                        throw new InvalidAppointmentStatusException(
                                        "Không thể cập nhật ghi chú khi lịch hẹn đang ở trạng thái "
                                                        + appointment.getStatus());
                }

                // Cập nhật note
                appointment.setNote(note);

                // Lưu vào CSDL
                appointment = appointmentRepository.save(appointment);

                // Chuyển sang DTO và trả về
                return convertToDTO(appointment);
        }

        // Phương thức hỗ trợ: Chuyển đổi từ entity sang DTO
        private AppointmentDTO convertToDTO(Appointment appointment) {
                return AppointmentDTO.builder()
                                .id(appointment.getId())
                                .name(appointment.getName())
                                .petName(appointment.getPetName())
                                .type(appointment.getType())
                                .breed(appointment.getBreed())
                                .healthStatus(appointment.getHealthStatus())
                                .healthHistory(appointment.getHealthHistory())
                                .note(appointment.getNote())
                                .status(appointment.getStatus())
                                .appointmentTime(appointment.getAppointmentTime())
                                .checkInTime(appointment.getCheckInTime())
                                .userId(appointment.getUser() != null ? appointment.getUser().getId() : null)
                                .userName(appointment.getUser() != null ? appointment.getUser().getUsername() : null)
                                .preferredDoctorId(
                                                appointment.getPreferredDoctor() != null
                                                                ? appointment.getPreferredDoctor().getId()
                                                                : null)
                                .preferredDoctorName(
                                                appointment.getPreferredDoctor() != null
                                                                ? appointment.getPreferredDoctor().getUsername()
                                                                : null)
                                .assignedDoctorId(
                                                appointment.getAssignedDoctor() != null
                                                                ? appointment.getAssignedDoctor().getId()
                                                                : null)
                                .assignedDoctorName(
                                                appointment.getAssignedDoctor() != null
                                                                ? appointment.getAssignedDoctor().getUsername()
                                                                : null)
                                .build();
        }
}