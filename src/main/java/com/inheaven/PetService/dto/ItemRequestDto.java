package com.inheaven.PetService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class ItemRequestDto {

    @NotBlank(message = "Tên sản phẩm không được để trống") // Kiểm tra tên không được để trống
    private String name; // Tên sản phẩm

    @NotNull(message = "Giá sản phẩm không được để trống") // Kiểm tra giá không được null
    @Min(value = 0, message = "Giá sản phẩm phải lớn hơn hoặc bằng 0") // Kiểm tra giá phải >= 0
    private BigDecimal price; // Giá gốc của sản phẩm

    private String imageUrl; // Đường dẫn đến hình ảnh sản phẩm

    @NotBlank(message = "Loại sản phẩm không được để trống")
    private String type; // Loại sản phẩm

    @NotBlank(message = "Danh mục sản phẩm không được để trống")
    private String category; // Danh mục sản phẩm

    private BigDecimal discount; // Giảm giá tính theo % (ví dụ: 10 tương đương 10%)

    @NotNull(message = "Số lượng sản phẩm không được để trống")
    @Min(value = 0, message = "Số lượng sản phẩm phải lớn hơn hoặc bằng 0")
    private Integer quantity; // Số lượng trong kho

    private String description; // Mô tả sản phẩm
}