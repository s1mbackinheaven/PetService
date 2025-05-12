package com.inheaven.PetService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String message; // Thông báo
    private boolean success; // Trạng thái thành công hay thất bại
}