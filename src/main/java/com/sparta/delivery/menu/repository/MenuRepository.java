package com.sparta.delivery.menu.repository;

import com.sparta.delivery.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByDeletedFalse(); // deleted = false인 것만 조회
    
    Optional<Menu> findByIdAndDeletedFalse(Long id);    // 단건조회 & 수정 & 주문에서 전부 재사용할 메서드 
}