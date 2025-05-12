package com.inheaven.PetService.dto;

import com.inheaven.PetService.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class ItemResponseDto {

    private Long id; // ID của sản phẩm
    private String name; // Tên sản phẩm
    private BigDecimal price; // Giá gốc của sản phẩm
    private String type; // Loại sản phẩm
    private String category; // Danh mục sản phẩm
    private BigDecimal discount; // Giảm giá (có thể null)
    private BigDecimal discountedPrice; // Giá sau khi giảm giá
    private Integer quantity; // Số lượng trong kho
    private String description; // Mô tả sản phẩm
    private String imageUrl; // Đường dẫn đến hình ảnh sản phẩm
    private ItemStatus status; // Trạng thái sản phẩm
    private Date createdAt; // Thời gian tạo sản phẩm
    private Date updatedAt; // Thời gian cập nhật sản phẩm gần nhất
    private Integer soldCount; // Số lượng đã bán
}