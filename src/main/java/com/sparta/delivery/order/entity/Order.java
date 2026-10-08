package com.sparta.delivery.order.entity;

import com.sparta.delivery.global.entity.BaseEntity;
import com.sparta.delivery.menu.entity.Menu;
import com.sparta.delivery.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 주문여러건이 손님1명을 가리킨다
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)  // 주문여러건이 메뉴1개를 가리킨다
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    public Order(User customer, Menu menu, int quantity, Integer totalPrice, String deliveryAddress) {
        this.customer = customer;
        this.menu = menu;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.deliveryAddress = deliveryAddress;
        this.status = OrderStatus.ORDERED;  // default값은 '주문요청'
    }

    public void pay() { this.status = OrderStatus.PAID; }

    public void cancel() { this.status = OrderStatus.CANCELED; }

    public void accept() { this.status = OrderStatus.ACCEPTED; }

    public void complete() { this.status = OrderStatus.COMPLETED; }
}