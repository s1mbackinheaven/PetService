package com.inheaven.PetService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity // Đánh dấu đây là một entity (bảng trong DB)
@Table(name = "invoice_items") // Đặt tên bảng trong database
@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class InvoiceItem {

    @Id // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động sinh giá trị cho khóa chính
    private Long id; // ID của mối quan hệ invoice-item

    @ManyToOne // Quan hệ nhiều-một với Invoice
    @JoinColumn(name = "invoice_id", nullable = false) // Join column với bảng invoice
    private Invoice invoice; // Invoice mà item này thuộc về

    @ManyToOne // Quan hệ nhiều-một với Item
    @JoinColumn(name = "item_id", nullable = false) // Join column với bảng item
    private Item item; // Item được mua

    @Column(nullable = false)
    private Integer quantity; // Số lượng item được mua

    @Column(nullable = false)
    private BigDecimal price; // Giá của item tại thời điểm mua

    private BigDecimal discount; // Giảm giá theo phần trăm (%)

    @Column(name = "subtotal")
    private BigDecimal subtotal; // Tổng giá sau khi đã áp dụng giảm giá

    // Phương thức được gọi trước khi lưu entity
    @PrePersist
    @PreUpdate
    protected void calculateSubtotal() {
        // Tính tổng tiền cho item này
        if (price != null && quantity != null) {
            // Tính tổng giá trước khi giảm giá
            BigDecimal totalBeforeDiscount = price.multiply(new BigDecimal(quantity));

            // Áp dụng giảm giá nếu có
            if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
                // Tính số tiền được giảm: totalBeforeDiscount * (discount / 100)
                BigDecimal discountAmount = totalBeforeDiscount.multiply(discount).divide(new BigDecimal(100));
                // Tính giá sau khi giảm
                subtotal = totalBeforeDiscount.subtract(discountAmount);
            } else {
                // Không có giảm giá
                subtotal = totalBeforeDiscount;
            }
        }
    }
}