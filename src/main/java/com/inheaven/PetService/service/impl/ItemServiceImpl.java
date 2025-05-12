package com.inheaven.PetService.service.impl;

import com.inheaven.PetService.dto.ItemRequestDto;
import com.inheaven.PetService.dto.ItemResponseDto;
import com.inheaven.PetService.entity.Item;
import com.inheaven.PetService.enums.ItemStatus;
import com.inheaven.PetService.exception.ResourceNotFoundException;
import com.inheaven.PetService.repository.ItemRepository;
import com.inheaven.PetService.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service // Đánh dấu đây là một service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository; // Repository để thao tác với database

    @Autowired // Tiêm phụ thuộc qua constructor
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Chuyển đổi từ Entity sang DTO để trả về cho client
    private ItemResponseDto convertToDto(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .type(item.getType())
                .category(item.getCategory())
                .discount(item.getDiscount())
                .discountedPrice(item.getDiscountedPrice())
                .quantity(item.getQuantity())
                .description(item.getDescription())
                .imageUrl(item.getImageUrl())
                .status(item.getStatus())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .soldCount(item.getSoldCount())
                .build();
    }

    // Chuyển đổi từ DTO sang Entity để lưu vào database
    private Item convertToEntity(ItemRequestDto requestDto) {
        return Item.builder()
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .type(requestDto.getType())
                .category(requestDto.getCategory())
                .discount(requestDto.getDiscount())
                .quantity(requestDto.getQuantity())
                .description(requestDto.getDescription())
                .imageUrl(requestDto.getImageUrl())
                .status(requestDto.getQuantity() > 0 ? ItemStatus.IN_STOCK : ItemStatus.OUT_OF_STOCK)
                .build();
    }

    @Override
    public ItemResponseDto createItem(ItemRequestDto requestDto) {
        // Chuyển từ DTO sang Entity
        Item item = convertToEntity(requestDto);

        // Lưu vào database
        Item savedItem = itemRepository.save(item);

        // Chuyển từ Entity sang DTO để trả về
        return convertToDto(savedItem);
    }

    @Override
    public ItemResponseDto updateItem(Long id, ItemRequestDto requestDto) {
        // Tìm item theo id
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        // Cập nhật thông tin
        existingItem.setName(requestDto.getName());
        existingItem.setPrice(requestDto.getPrice());
        existingItem.setType(requestDto.getType());
        existingItem.setCategory(requestDto.getCategory());
        existingItem.setDiscount(requestDto.getDiscount());
        existingItem.setQuantity(requestDto.getQuantity());
        existingItem.setDescription(requestDto.getDescription());
        existingItem.setImageUrl(requestDto.getImageUrl());

        // Lưu vào database
        Item updatedItem = itemRepository.save(existingItem);

        // Chuyển từ Entity sang DTO để trả về
        return convertToDto(updatedItem);
    }

    @Override
    public void deleteItem(Long id) {
        // Kiểm tra xem item có tồn tại không
        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id);
        }

        // Xóa item
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemResponseDto> getAllItems() {
        // Lấy tất cả items từ database và chuyển đổi sang DTO
        return itemRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto getItemById(Long id) {
        // Tìm item theo id
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        // Chuyển từ Entity sang DTO để trả về
        return convertToDto(item);
    }

    @Override
    public List<ItemResponseDto> getItemsByCategory(String category) {
        // Tìm items theo danh mục và chuyển đổi sang DTO
        return itemRepository.findByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDto> getItemsByType(String type) {
        // Tìm items theo loại và chuyển đổi sang DTO
        return itemRepository.findByType(type).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDto> getItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        // Tìm items trong khoảng giá và chuyển đổi sang DTO
        return itemRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDto> searchItemsByKeyword(String keyword) {
        // Tìm items theo từ khóa và chuyển đổi sang DTO
        return itemRepository.searchByKeyword(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDto> getDiscountedItems() {
        // Tìm items đang giảm giá và chuyển đổi sang DTO
        return itemRepository.findByDiscountIsNotNull().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDto> getLowStockItems(Integer threshold) {
        // Tìm items sắp hết hàng và chuyển đổi sang DTO
        return itemRepository.findByQuantityLessThan(threshold).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDto> getBestSellingItems() {
        // Tìm items bán chạy nhất và chuyển đổi sang DTO
        return itemRepository.findAllByOrderBySoldCountDesc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDto> getLatestItems() {
        // Tìm items mới nhất và chuyển đổi sang DTO
        return itemRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}