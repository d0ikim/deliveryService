package com.sparta.delivery.payment.dto.request;

import com.sparta.delivery.payment.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PaymentRequest {
    @NotNull
    private PaymentMethod method;
}