package com.inheaven.PetService.exception;

public class EmptyQueueException extends RuntimeException {

    public EmptyQueueException() {
        super("Hàng đợi lịch hẹn hiện đang trống");
    }

    public EmptyQueueException(String message) {
        super(message);
    }
}