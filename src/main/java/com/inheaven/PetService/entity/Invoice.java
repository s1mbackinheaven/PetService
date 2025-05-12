package com.inheaven.PetService.entity;

import com.inheaven.PetService.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity // Đánh dấu đây là một entity (bảng trong DB)
@Table(name = "invoices") // Đặt tên bảng trong database
@Data // Lombok: tự động tạo getter, setter, toString, equals, hashcode
@NoArgsConstructor // Tạo constructor mặc định
@AllArgsConstructor // Tạo constructor có tham số
@Builder // Cho phép sử dụng Builder pattern để khởi tạo đối tượng
public class Invoice {

    @Id // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động sinh giá trị cho khóa chính
    private Long id; // ID của hóa đơn

    @ManyToOne // Quan hệ nhiều-một với User
    @JoinColumn(name = "user_id", nullable = false) // Join column với bảng user
    private User user; // Người dùng tạo đơn hàng

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true) // Quan hệ một-nhiều với
                                                                                      // InvoiceItem
    @Builder.Default // Sử dụng giá trị mặc định khi khởi tạo với Builder
    private List<InvoiceItem> invoiceItems = new ArrayList<>(); // Danh sách các sản phẩm trong đơn hàng

    @Column(name = "total_items")
    @Builder.Default // Sử dụng giá trị mặc định khi khởi tạo với Builder
    private Integer totalItems = 0; // Tổng số lượng sản phẩm trong đơn hàng

    @Column(name = "total_amount")
    @Builder.Default // Sử dụng giá trị mặc định khi khởi tạo với Builder
    private BigDecimal totalAmount = BigDecimal.ZERO; // Tổng tiền của đơn hàng

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status; // Trạng thái đơn hàng

    @Column(length = 500)
    private String shippingAddress; // Địa chỉ giao hàng

    private String paymentMethod; // Phương thức thanh toán

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt; // Thời gian tạo đơn hàng

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt; // Thời gian cập nhật đơn hàng

    @Temporal(TemporalType.TIMESTAMP)
    private Date paidAt; // Thời gian thanh toán đơn hàng

    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt; // Thời gian hoàn thành đơn hàng

    // Phương thức helper để thêm item vào invoice
    public void addInvoiceItem(InvoiceItem invoiceItem) {
        if (invoiceItems == null) {
            invoiceItems = new ArrayList<>();
        }
        invoiceItems.add(invoiceItem);
        invoiceItem.setInvoice(this);
        recalculateTotals();
    }

    // Phương thức helper để xóa item khỏi invoice
    public void removeInvoiceItem(InvoiceItem invoiceItem) {
        if (invoiceItems != null) {
            invoiceItems.remove(invoiceItem);
            invoiceItem.setInvoice(null);
            recalculateTotals();
        }
    }

    // Tính lại tổng số lượng và tổng tiền
    public void recalculateTotals() {
        if (invoiceItems == null || invoiceItems.isEmpty()) {
            totalItems = 0;
            totalAmount = BigDecimal.ZERO;
            return;
        }

        totalItems = invoiceItems.stream()
                .filter(item -> item.getQuantity() != null)
                .mapToInt(InvoiceItem::getQuantity)
                .sum();

        totalAmount = invoiceItems.stream()
                .filter(item -> item.getSubtotal() != null)
                .map(InvoiceItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Phương thức được gọi trước khi lưu entity
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();

        if (status == null) {
            status = InvoiceStatus.PENDING;
        }

        recalculateTotals();
    }

    // Phương thức được gọi trước khi cập nhật entity
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();

        // Cập nhật thời gian thanh toán nếu trạng thái là đã thanh toán
        if (status == InvoiceStatus.PAID && paidAt == null) {
            paidAt = new Date();
        }

        // Cập nhật thời gian hoàn thành nếu trạng thái là thành công
        if (status == InvoiceStatus.COMPLETED && completedAt == null) {
            completedAt = new Date();
        }

        recalculateTotals();
    }
}