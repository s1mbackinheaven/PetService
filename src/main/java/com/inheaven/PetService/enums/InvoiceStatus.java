package com.inheaven.PetService.enums;

public enum InvoiceStatus {
    PENDING("Đang thanh toán"), // Đơn hàng đang chờ thanh toán
    CANCELED("Đã hủy"), // Đơn hàng đã bị hủy
    PAID("Đã thanh toán"), // Đơn hàng đã thanh toán nhưng chưa hoàn thành
    COMPLETED("Thành công"); // Đơn hàng đã hoàn thành thành công

    private final String displayValue; // Giá trị hiển thị

    InvoiceStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}