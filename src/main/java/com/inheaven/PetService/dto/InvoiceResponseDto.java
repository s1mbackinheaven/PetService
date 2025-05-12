package com.inheaven.PetService.dto;

import com.inheaven.PetService.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class InvoiceResponseDto {

    private Long id; // ID của hóa đơn

    private Long userId; // ID của người dùng tạo đơn hàng

    private String userName; // Tên người dùng tạo đơn hàng

    private List<InvoiceItemDto> items; // Danh sách các sản phẩm trong đơn hàng

    private Integer totalItems; // Tổng số lượng sản phẩm trong đơn hàng

    private BigDecimal totalAmount; // Tổng tiền của đơn hàng

    private InvoiceStatus status; // Trạng thái đơn hàng

    private String statusDisplayValue; // Giá trị hiển thị của trạng thái

    private String shippingAddress; // Địa chỉ giao hàng

    private String paymentMethod; // Phương thức thanh toán

    private Date createdAt; // Thời gian tạo đơn hàng

    private Date updatedAt; // Thời gian cập nhật đơn hàng

    private Date paidAt; // Thời gian thanh toán đơn hàng

    private Date completedAt; // Thời gian hoàn thành đơn hàng
}