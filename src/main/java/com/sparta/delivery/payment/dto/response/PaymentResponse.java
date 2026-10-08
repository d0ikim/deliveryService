package com.sparta.delivery.payment.dto.response;

import com.sparta.delivery.payment.entity.Payment;
import com.sparta.delivery.payment.entity.PaymentMethod;
import com.sparta.delivery.payment.entity.PaymentStatus;
import lombok.Getter;

@Getter
public class PaymentResponse {
    private final Long id;
    private final Long orderId;
    private final Integer amount;
    private final PaymentMethod method;
    private final PaymentStatus status;


    public PaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.orderId = payment.getOrder().getId();
        this.amount = payment.getAmount();
        this.method = payment.getMethod();
        this.status = payment.getStatus();
    }
}