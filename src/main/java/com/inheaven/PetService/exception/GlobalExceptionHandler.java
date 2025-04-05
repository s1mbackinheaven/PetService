package com.inheaven.PetService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice // Đánh dấu đây là một controller advice để xử lý exception
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Xử lý exception UserAlreadyExistsException
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        // Trả về response với status 409 (Conflict) và message của exception
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // Xử lý exception UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        // Trả về response với status 404 (Not Found) và message của exception
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Xử lý các exception khác
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        // Trả về response với status 500 (Internal Server Error) và message của
        // exception
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi: " + ex.getMessage());
    }
}