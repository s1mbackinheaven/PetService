package com.inheaven.PetService.service;

import com.inheaven.PetService.dto.ItemRequestDto;
import com.inheaven.PetService.dto.ItemResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface ItemService {

    // Tạo mới một sản phẩm
    ItemResponseDto createItem(ItemRequestDto requestDto);

    // Cập nhật thông tin sản phẩm
    ItemResponseDto updateItem(Long id, ItemRequestDto requestDto);

    // Xóa sản phẩm
    void deleteItem(Long id);

    // Lấy tất cả sản phẩm
    List<ItemResponseDto> getAllItems();

    // Lấy thông tin sản phẩm theo ID
    ItemResponseDto getItemById(Long id);

    // Lấy sản phẩm theo danh mục
    List<ItemResponseDto> getItemsByCategory(String category);

    // Lấy sản phẩm theo loại
    List<ItemResponseDto> getItemsByType(String type);

    // Lấy sản phẩm trong khoảng giá từ minPrice đến maxPrice
    List<ItemResponseDto> getItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    // Tìm kiếm sản phẩm theo từ khóa
    List<ItemResponseDto> searchItemsByKeyword(String keyword);

    // Lấy sản phẩm đang giảm giá
    List<ItemResponseDto> getDiscountedItems();

    // Lấy sản phẩm sắp hết hàng
    List<ItemResponseDto> getLowStockItems(Integer threshold);

    // Lấy sản phẩm bán chạy nhất
    List<ItemResponseDto> getBestSellingItems();

    // Lấy sản phẩm mới nhất
    List<ItemResponseDto> getLatestItems();
}