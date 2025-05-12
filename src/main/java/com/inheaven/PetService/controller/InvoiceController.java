package com.inheaven.PetService.controller;

import com.inheaven.PetService.dto.InvoiceRequestDto;
import com.inheaven.PetService.dto.InvoiceResponseDto;
import com.inheaven.PetService.dto.UpdateInvoiceStatusDto;
import com.inheaven.PetService.enums.InvoiceStatus;
import com.inheaven.PetService.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController // Đánh dấu đây là một controller REST
@RequestMapping("/api/invoices") // Định nghĩa đường dẫn cơ sở cho tất cả các endpoint
@CrossOrigin(origins = "*") // Cho phép gọi API từ bất kỳ nguồn nào
public class InvoiceController {

    private final InvoiceService invoiceService; // Service để xử lý logic nghiệp vụ

    @Autowired // Tiêm phụ thuộc qua constructor
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // Tạo mới một hóa đơn
    @PostMapping
    public ResponseEntity<InvoiceResponseDto> createInvoice(@Valid @RequestBody InvoiceRequestDto requestDto) {
        InvoiceResponseDto createdInvoice = invoiceService.createInvoice(requestDto);
        return new ResponseEntity<>(createdInvoice, HttpStatus.CREATED);
    }

    // Cập nhật thông tin hóa đơn
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponseDto> updateInvoice(
            @PathVariable Long id,
            @Valid @RequestBody InvoiceRequestDto requestDto) {
        InvoiceResponseDto updatedInvoice = invoiceService.updateInvoice(id, requestDto);
        return ResponseEntity.ok(updatedInvoice);
    }

    // Admin cập nhật trạng thái hóa đơn (đã thanh toán, đã hủy, etc.)
    @PutMapping("/{id}/status")
    public ResponseEntity<InvoiceResponseDto> updateInvoiceStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInvoiceStatusDto requestDto) {
        InvoiceResponseDto updatedInvoice = invoiceService.updateInvoiceStatus(id, requestDto);
        return ResponseEntity.ok(updatedInvoice);
    }

    // User xác nhận giao hàng thành công
    @PutMapping("/{id}/confirm-delivery")
    public ResponseEntity<InvoiceResponseDto> confirmDelivery(@PathVariable Long id) {
        InvoiceResponseDto updatedInvoice = invoiceService.confirmDelivery(id);
        return ResponseEntity.ok(updatedInvoice);
    }

    // Hủy đơn hàng
    @PutMapping("/{id}/cancel")
    public ResponseEntity<InvoiceResponseDto> cancelInvoice(@PathVariable Long id) {
        InvoiceResponseDto canceledInvoice = invoiceService.cancelInvoice(id);
        return ResponseEntity.ok(canceledInvoice);
    }

    // Xóa hóa đơn
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }

    // Lấy tất cả hóa đơn (thường chỉ dành cho admin)
    @GetMapping
    public ResponseEntity<List<InvoiceResponseDto>> getAllInvoices() {
        List<InvoiceResponseDto> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }

    // Lấy thông tin hóa đơn theo ID
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceById(@PathVariable Long id) {
        InvoiceResponseDto invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(invoice);
    }

    // Lấy tất cả hóa đơn của một người dùng
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<InvoiceResponseDto>> getInvoicesByUserId(@PathVariable Long userId) {
        List<InvoiceResponseDto> invoices = invoiceService.getInvoicesByUserId(userId);
        return ResponseEntity.ok(invoices);
    }

    // Lấy tất cả hóa đơn của một người dùng theo trạng thái
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<InvoiceResponseDto>> getInvoicesByUserIdAndStatus(
            @PathVariable Long userId,
            @PathVariable InvoiceStatus status) {
        List<InvoiceResponseDto> invoices = invoiceService.getInvoicesByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(invoices);
    }

    // Lấy tất cả hóa đơn đã thanh toán hoặc hoàn thành của một người dùng
    @GetMapping("/user/{userId}/paid-completed")
    public ResponseEntity<List<InvoiceResponseDto>> getPaidOrCompletedInvoicesByUserId(@PathVariable Long userId) {
        List<InvoiceResponseDto> invoices = invoiceService.getPaidOrCompletedInvoicesByUserId(userId);
        return ResponseEntity.ok(invoices);
    }

    // Lấy tất cả hóa đơn theo trạng thái
    @GetMapping("/status/{status}")
    public ResponseEntity<List<InvoiceResponseDto>> getInvoicesByStatus(@PathVariable InvoiceStatus status) {
        List<InvoiceResponseDto> invoices = invoiceService.getInvoicesByStatus(status);
        return ResponseEntity.ok(invoices);
    }

    // Lấy tất cả hóa đơn được tạo trong khoảng thời gian
    @GetMapping("/date-range")
    public ResponseEntity<List<InvoiceResponseDto>> getInvoicesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        List<InvoiceResponseDto> invoices = invoiceService.getInvoicesByDateRange(startDate, endDate);
        return ResponseEntity.ok(invoices);
    }

    // Thống kê số lượng đơn hàng theo trạng thái
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countInvoicesByStatus(@PathVariable InvoiceStatus status) {
        long count = invoiceService.countInvoicesByStatus(status);
        return ResponseEntity.ok(count);
    }
}