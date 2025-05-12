package com.inheaven.PetService.repository;

import com.inheaven.PetService.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Đánh dấu đây là một Repository bean
public interface PetRepository extends JpaRepository<Pet, Long> {
    // Kế thừa JpaRepository để có sẵn các phương thức CRUD cơ bản
    // Pet: kiểu entity, Long: kiểu dữ liệu của khóa chính

    // Tìm tất cả pet của một user
    List<Pet> findByUserId(Long userId);
}