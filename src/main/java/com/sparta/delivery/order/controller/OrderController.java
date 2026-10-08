package com.sparta.delivery.order.controller;

import com.sparta.delivery.order.dto.request.OrderRequest;
import com.sparta.delivery.order.dto.response.OrderResponse;
import com.sparta.delivery.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "주문", description = "주문 생성·조회·취소·상태변경 API")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // 8. 주문 생성
    @Operation(summary = "주문 생성", description = "손님(CUSTOMER)이 메뉴를 주문합니다. 총액(메뉴가격 x 수량)은 서버가 계산하며, 초기 상태는 '주문요청(ORDERED)'입니다.")
    @PreAuthorize("hasRole('CUSTOMER')")    // 손님(주문자) 전용 기능
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@AuthenticationPrincipal String username, @Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.create(username, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 9. 주문목록 조회
    @Operation(summary = "주문 목록 조회", description = "로그인한 사용자의 역할에 따라 다른 목록을 반환합니다. 손님(CUSTOMER)은 본인이 한 주문만, 사장님(OWNER)은 본인 메뉴에 들어온 주문만 조회됩니다.")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(@AuthenticationPrincipal String username) {
        return ResponseEntity.ok(orderService.getOrders(username));
    }

    // 10. 주문 취소
    @Operation(summary = "주문 취소", description = "본인 주문만 취소할 수 있습니다. '주문요청(ORDERED)' 상태일 때만 취소가 가능하며, 결제가 끝난 주문은 거절됩니다.")
    @PreAuthorize("hasRole('CUSTOMER')")    // 손님(주문자) 전용 기능
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@AuthenticationPrincipal String username, @PathVariable Long orderId) {
        orderService.cancel(username, orderId);
        return ResponseEntity.noContent().build();  // 204
    }

    // 11. 주문상태 변경
    @Operation(summary = "주문 상태 변경", description = "본인 메뉴에 들어온 주문만 처리할 수 있습니다. '결제완료→주문수락', '주문수락→배달완료' 두 가지 전이만 허용되며, 그 외 상태 변경은 거절됩니다.")
    @PreAuthorize("hasRole('OWNER')")   // 사장님만 쓸수있는 기능
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(@AuthenticationPrincipal String username, @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.updateStatus(username, orderId)); // 바뀐 주문정보를 보여주는게 실용적이어서 200 OK + 바디
    }
}