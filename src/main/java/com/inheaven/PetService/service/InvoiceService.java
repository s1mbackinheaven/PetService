package com.inheaven.PetService.service;

import com.inheaven.PetService.dto.InvoiceRequestDto;
import com.inheaven.PetService.dto.InvoiceResponseDto;
import com.inheaven.PetService.dto.UpdateInvoiceStatusDto;
import com.inheaven.PetService.enums.InvoiceStatus;

import java.util.Date;
import java.util.List;

public interface InvoiceService {

    // Tạo mới một hóa đơn
    InvoiceResponseDto createInvoice(InvoiceRequestDto requestDto);

    // Cập nhật thông tin hóa đơn
    InvoiceResponseDto updateInvoice(Long id, InvoiceRequestDto requestDto);

    // Cập nhật trạng thái hóa đơn (admin)
    InvoiceResponseDto updateInvoiceStatus(Long id, UpdateInvoiceStatusDto requestDto);

    // Xác nhận giao hàng thành công (user)
    InvoiceResponseDto confirmDelivery(Long id);

    // Hủy đơn hàng
    InvoiceResponseDto cancelInvoice(Long id);

    // Xóa hóa đơn
    void deleteInvoice(Long id);

    // Lấy tất cả hóa đơn
    List<InvoiceResponseDto> getAllInvoices();

    // Lấy thông tin hóa đơn theo ID
    InvoiceResponseDto getInvoiceById(Long id);

    // Lấy tất cả hóa đơn của một người dùng
    List<InvoiceResponseDto> getInvoicesByUserId(Long userId);

    // Lấy tất cả hóa đơn của một người dùng theo trạng thái
    List<InvoiceResponseDto> getInvoicesByUserIdAndStatus(Long userId, InvoiceStatus status);

    // Lấy tất cả hóa đơn đã thanh toán hoặc hoàn thành của một người dùng
    List<InvoiceResponseDto> getPaidOrCompletedInvoicesByUserId(Long userId);

    // Lấy tất cả hóa đơn theo trạng thái
    List<InvoiceResponseDto> getInvoicesByStatus(InvoiceStatus status);

    // Lấy tất cả hóa đơn được tạo trong khoảng thời gian
    List<InvoiceResponseDto> getInvoicesByDateRange(Date startDate, Date endDate);

    // Thống kê số lượng đơn hàng theo trạng thái
    long countInvoicesByStatus(InvoiceStatus status);
}