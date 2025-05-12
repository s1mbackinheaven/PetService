package com.inheaven.PetService.repository;

import com.inheaven.PetService.entity.Invoice;
import com.inheaven.PetService.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository // Đánh dấu đây là một repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // Tìm tất cả các đơn hàng của một người dùng
    List<Invoice> findByUserId(Long userId);

    // Tìm tất cả các đơn hàng của một người dùng theo trạng thái
    List<Invoice> findByUserIdAndStatus(Long userId, InvoiceStatus status);

    // Tìm tất cả các đơn hàng của một người dùng đã thanh toán hoặc hoàn thành
    List<Invoice> findByUserIdAndStatusIn(Long userId, List<InvoiceStatus> statuses);

    // Tìm tất cả đơn hàng theo trạng thái
    List<Invoice> findByStatus(InvoiceStatus status);

    // Tìm tất cả đơn hàng được tạo trong khoảng thời gian
    List<Invoice> findByCreatedAtBetween(Date startDate, Date endDate);

    // Tìm tất cả đơn hàng theo phương thức thanh toán
    List<Invoice> findByPaymentMethod(String paymentMethod);

    // Đếm số lượng đơn hàng theo trạng thái
    long countByStatus(InvoiceStatus status);
}