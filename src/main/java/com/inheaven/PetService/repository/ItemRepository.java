package com.inheaven.PetService.repository;

import com.inheaven.PetService.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository // Đánh dấu đây là một repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Tìm tất cả các sản phẩm theo danh mục
    List<Item> findByCategory(String category);

    // Tìm tất cả các sản phẩm theo loại
    List<Item> findByType(String type);

    // Tìm tất cả các sản phẩm có giá trong khoảng từ minPrice đến maxPrice
    List<Item> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Tìm tất cả các sản phẩm có giá sau giảm giá trong khoảng từ minPrice đến
    // maxPrice
    List<Item> findByDiscountedPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Tìm kiếm sản phẩm theo tên (chứa từ khóa)
    List<Item> findByNameContainingIgnoreCase(String keyword);

    // Tìm kiếm sản phẩm theo tên hoặc mô tả chứa từ khóa
    @Query("SELECT i FROM Item i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Item> searchByKeyword(@Param("keyword") String keyword);

    // Tìm các sản phẩm đang giảm giá
    List<Item> findByDiscountIsNotNull();

    // Tìm các sản phẩm có số lượng dưới mức cảnh báo (ví dụ: < 10)
    List<Item> findByQuantityLessThan(Integer threshold);

    // Tìm các sản phẩm bán chạy nhất (theo số lượng đã bán - giảm dần)
    List<Item> findAllByOrderBySoldCountDesc();

    // Tìm các sản phẩm mới nhất (theo thời gian tạo - giảm dần)
    List<Item> findAllByOrderByCreatedAtDesc();
}