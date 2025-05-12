package com.inheaven.PetService.enums;

public enum AppointmentStatus {
    SCHEDULED, // Đã đặt lịch
    CHECKED_IN, // Đã check-in (đã đến)
    IN_PROGRESS, // Đang khám (bác sĩ đã gọi)
    COMPLETED, // Đã hoàn thành thăm khám
    CANCELLED // Đã hủy
}