package com.inheaven.PetService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.inheaven.PetService.enums.ItemStatus;

import java.math.BigDecimal;
import java.util.Date;

@Entity // Đánh dấu đây là một entity (bảng trong DB)
@Table(name = "items") // Đặt tên bảng trong database
@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class Item {

    @Id // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động sinh giá trị cho khóa chính
    private Long id; // ID của sản phẩm

    @Column(nullable = false) // Trường bắt buộc, không được null
    private String name; // Tên sản phẩm

    @Column(nullable = false)
    private BigDecimal price; // Giá gốc của sản phẩm

    private String type; // Loại sản phẩm (ví dụ: thức ăn, đồ chơi, thuốc)

    private String category; // Danh mục sản phẩm (ví dụ: dành cho chó, mèo, chim)

    @Column(name = "discount_percent")
    private BigDecimal discount; // Giảm giá (%), nếu null thì mặc định là 0%

    @Column(name = "discounted_price")
    private BigDecimal discountedPrice; // Giá sau khi giảm giá (tự động tính)

    @Column(nullable = false)
    private Integer quantity; // Số lượng trong kho

    @Column(length = 200000) // Cấu hình độ dài lớn hơn cho mô tả
    private String description; // Mô tả sản phẩm

    @Column(name = "image_url")
    private String imageUrl; // Đường dẫn đến hình ảnh sản phẩm

    @Enumerated(EnumType.STRING)
    private ItemStatus status; // Trạng thái: CÒN HÀNG, HẾT HÀNG

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt; // Thời gian tạo sản phẩm

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt; // Thời gian cập nhật sản phẩm gần nhất

    @Column(name = "sold_count")
    @Builder.Default // Sử dụng giá trị mặc định khi khởi tạo với Builder
    private Integer soldCount = 0; // Số lượng đã bán, mặc định là 0

    // Phương thức được gọi trước khi lưu entity
    @PrePersist
    protected void onCreate() {
        createdAt = new Date(); // Thiết lập thời gian tạo
        updatedAt = new Date(); // Thiết lập thời gian cập nhật ban đầu

        // Tính giá sau khi giảm giá
        calculateDiscountedPrice();

        // Thiết lập trạng thái mặc định
        if (status == null) {
            status = quantity > 0 ? ItemStatus.IN_STOCK : ItemStatus.OUT_OF_STOCK;
        }
    }

    // Phương thức được gọi trước khi cập nhật entity
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date(); // Cập nhật thời gian

        // Cập nhật giá sau giảm giá
        calculateDiscountedPrice();

        // Cập nhật trạng thái dựa trên số lượng
        status = quantity > 0 ? ItemStatus.IN_STOCK : ItemStatus.OUT_OF_STOCK;
    }

    // Phương thức tính giá sau khi giảm giá
    private void calculateDiscountedPrice() {
        // Nếu discount là null, đặt mặc định là 0%
        if (discount == null) {
            discount = BigDecimal.ZERO;
            discountedPrice = price; // Giá sau giảm = giá gốc
        } else {
            // Tính giá sau khi giảm theo công thức: price - (price * discount / 100)
            BigDecimal discountAmount = price.multiply(discount).divide(new BigDecimal(100));
            discountedPrice = price.subtract(discountAmount);
        }
    }
}