package com.sparta.delivery.order.dto.response;

import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.order.entity.OrderStatus;
import lombok.Getter;

@Getter
public class OrderResponse {
    private final Long id;
    private final Long menuId;
    private final String menuName;
    private final int quantity;
    private final Integer totalPrice;
    private final String deliveryAddress;
    private final OrderStatus status;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.menuId = order.getMenu().getId();
        this.menuName = order.getMenu().getName();  // LAZY로딩 발동
        this.quantity = order.getQuantity();
        this.totalPrice = order.getTotalPrice();
        this.deliveryAddress = order.getDeliveryAddress();
        this.status = order.getStatus();
    }
}