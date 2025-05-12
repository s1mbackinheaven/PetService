package com.inheaven.PetService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class InvoiceItemDto {

    private Long id; // ID của mối quan hệ invoice-item

    @NotNull(message = "ID của sản phẩm không được để trống")
    private Long itemId; // ID của sản phẩm

    private String itemName; // Tên sản phẩm

    @NotNull(message = "Số lượng sản phẩm không được để trống")
    @Min(value = 1, message = "Số lượng sản phẩm phải lớn hơn 0")
    private Integer quantity; // Số lượng sản phẩm được mua

    private BigDecimal price; // Giá của sản phẩm

    private BigDecimal discount; // Giảm giá theo phần trăm (%)

    private BigDecimal subtotal; // Tổng giá sau khi áp dụng giảm giá
}