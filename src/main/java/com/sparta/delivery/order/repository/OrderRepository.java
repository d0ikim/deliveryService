package com.sparta.delivery.order.repository;

import com.sparta.delivery.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Order주문의 > Customer(User)손님필드의 > username이름이 일치하는 것들
    List<Order> findAllByCustomerUsername(String username);

    // Order주문의 > menu(Menu)메뉴필드의 > owner(User)주인필드의 > username이름이 일치하는 것들
    // OWNER가 "내 메뉴에 들어온 주문"을 조회
    List<Order> findAllByMenuOwnerUsername(String username);
}