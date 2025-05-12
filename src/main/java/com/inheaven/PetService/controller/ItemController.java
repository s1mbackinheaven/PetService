package com.inheaven.PetService.controller;

import com.inheaven.PetService.dto.ItemRequestDto;
import com.inheaven.PetService.dto.ItemResponseDto;
import com.inheaven.PetService.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController // Đánh dấu đây là một controller REST
@RequestMapping("/api/items") // Định nghĩa đường dẫn cơ sở cho tất cả các endpoint
@CrossOrigin(origins = "*") // Cho phép gọi API từ bất kỳ nguồn nào
public class ItemController {

    private final ItemService itemService; // Service để xử lý logic nghiệp vụ

    @Autowired // Tiêm phụ thuộc qua constructor
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // Tạo mới một sản phẩm
    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@Valid @RequestBody ItemRequestDto requestDto) {
        ItemResponseDto createdItem = itemService.createItem(requestDto);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    // Cập nhật thông tin sản phẩm
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDto> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemRequestDto requestDto) {
        ItemResponseDto updatedItem = itemService.updateItem(id, requestDto);
        return ResponseEntity.ok(updatedItem);
    }

    // Xóa sản phẩm
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    // Lấy tất cả sản phẩm
    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAllItems() {
        List<ItemResponseDto> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    // Lấy thông tin sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getItemById(@PathVariable Long id) {
        ItemResponseDto item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    // Lấy sản phẩm theo danh mục
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ItemResponseDto>> getItemsByCategory(@PathVariable String category) {
        List<ItemResponseDto> items = itemService.getItemsByCategory(category);
        return ResponseEntity.ok(items);
    }

    // Lấy sản phẩm theo loại
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ItemResponseDto>> getItemsByType(@PathVariable String type) {
        List<ItemResponseDto> items = itemService.getItemsByType(type);
        return ResponseEntity.ok(items);
    }

    // Lấy sản phẩm trong khoảng giá
    @GetMapping("/price-range")
    public ResponseEntity<List<ItemResponseDto>> getItemsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<ItemResponseDto> items = itemService.getItemsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(items);
    }

    // Tìm kiếm sản phẩm theo từ khóa
    @GetMapping("/search")
    public ResponseEntity<List<ItemResponseDto>> searchItems(@RequestParam String keyword) {
        List<ItemResponseDto> items = itemService.searchItemsByKeyword(keyword);
        return ResponseEntity.ok(items);
    }

    // Lấy sản phẩm đang giảm giá
    @GetMapping("/discounted")
    public ResponseEntity<List<ItemResponseDto>> getDiscountedItems() {
        List<ItemResponseDto> items = itemService.getDiscountedItems();
        return ResponseEntity.ok(items);
    }

    // Lấy sản phẩm sắp hết hàng
    @GetMapping("/low-stock")
    public ResponseEntity<List<ItemResponseDto>> getLowStockItems(
            @RequestParam(defaultValue = "10") Integer threshold) {
        List<ItemResponseDto> items = itemService.getLowStockItems(threshold);
        return ResponseEntity.ok(items);
    }

    // Lấy sản phẩm bán chạy nhất
    @GetMapping("/best-selling")
    public ResponseEntity<List<ItemResponseDto>> getBestSellingItems() {
        List<ItemResponseDto> items = itemService.getBestSellingItems();
        return ResponseEntity.ok(items);
    }

    // Lấy sản phẩm mới nhất
    @GetMapping("/latest")
    public ResponseEntity<List<ItemResponseDto>> getLatestItems() {
        List<ItemResponseDto> items = itemService.getLatestItems();
        return ResponseEntity.ok(items);
    }
}