package com.inheaven.PetService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class InvoiceRequestDto {

    @NotNull(message = "ID của người dùng không được để trống")
    private Long userId; // ID của người dùng tạo đơn hàng

    @NotEmpty(message = "Danh sách sản phẩm không được để trống")
    @Valid // Xác thực các phần tử trong danh sách
    private List<InvoiceItemDto> items; // Danh sách các sản phẩm trong đơn hàng

    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    private String shippingAddress; // Địa chỉ giao hàng

    @NotBlank(message = "Phương thức thanh toán không được để trống")
    private String paymentMethod; // Phương thức thanh toán
}