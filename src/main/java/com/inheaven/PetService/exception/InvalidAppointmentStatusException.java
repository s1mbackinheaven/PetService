package com.inheaven.PetService.exception;

import com.inheaven.PetService.enums.AppointmentStatus;

public class InvalidAppointmentStatusException extends RuntimeException {

    public InvalidAppointmentStatusException(String message) {
        super(message);
    }

    public InvalidAppointmentStatusException(AppointmentStatus currentStatus, AppointmentStatus requiredStatus) {
        super("Không thể thực hiện thao tác này. Trạng thái hiện tại là " + currentStatus +
                ", yêu cầu trạng thái " + requiredStatus);
    }
}