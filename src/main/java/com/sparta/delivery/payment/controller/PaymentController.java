package com.sparta.delivery.payment.controller;

import com.sparta.delivery.payment.dto.request.PaymentRequest;
import com.sparta.delivery.payment.dto.response.PaymentResponse;
import com.sparta.delivery.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "결제", description = "결제 API")
@RestController
@RequestMapping("/api/orders/{orderId}/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "결제", description = "본인 주문만 결제할 수 있습니다. 결제 금액은 주문 총액을 서버가 그대로 사용하며, 주문요청(ORDERED) 상태일 때만 결제가 가능합니다. 성공 시 주문 상태가 결제완료(PAID)로 바뀝니다.")
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<PaymentResponse> pay(@AuthenticationPrincipal String username, @PathVariable Long orderId, @Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.pay(username, orderId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}