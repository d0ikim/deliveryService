package com.sparta.delivery.payment.entity;

import com.sparta.delivery.global.entity.BaseEntity;
import com.sparta.delivery.order.entity.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionId;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 결제여러건이 주문1건을 가리킨다(한 주문에 결제기록이 여러번 쌓일 수 있는 구조)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;


    public Payment(Order order, Integer amount, PaymentMethod method) {
        this.order = order;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.COMPLETED;  // 결제는 성공하면 바로 COMPLETED라서, 상태변경 별도메서드 없이 생성자에서 바로 고정
    }
}