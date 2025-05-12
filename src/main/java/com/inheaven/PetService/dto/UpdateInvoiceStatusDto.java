package com.inheaven.PetService.dto;

import com.inheaven.PetService.enums.InvoiceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class UpdateInvoiceStatusDto {

    @NotNull(message = "Trạng thái mới không được để trống")
    private InvoiceStatus status; // Trạng thái mới của đơn hàng
}