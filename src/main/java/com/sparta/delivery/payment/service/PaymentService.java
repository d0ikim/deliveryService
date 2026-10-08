package com.sparta.delivery.payment.service;

import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.order.entity.OrderStatus;
import com.sparta.delivery.order.repository.OrderRepository;
import com.sparta.delivery.payment.dto.request.PaymentRequest;
import com.sparta.delivery.payment.dto.response.PaymentResponse;
import com.sparta.delivery.payment.entity.Payment;
import com.sparta.delivery.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    // 12. 결제
    @Transactional
    public PaymentResponse pay(String username, Long orderId, PaymentRequest request) {
        // 주문번호로 해당 주문객체를 찾아 저장 (없으면 404)
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다"));

        // 해당주문의 > 주문자(손님)의 > 이름이 결제자(본인)과 일치하지 않으면, 결제불가
        if (!order.getCustomer().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 주문만 결제할 수 있습니다.");
        }
        // 해당주문의 > 주문상태값이 '주문요청'이 아니면, 결제불가
        if (order.getStatus() != OrderStatus.ORDERED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "주문요청 상태일때만 결제할 수 있습니다.");
        }

        // (주문객체, 주문객체의>총금액, request의 결제수단)정보를 포함한 새 결제객체 생성
        Payment payment = new Payment(order, order.getTotalPrice(), request.getMethod());
        Payment savedPayment = paymentRepository.save(payment); // 결제테이블에 결제객체 저장 후 대입

        order.pay();    // 해당 주문객체의 > 결제(주문상태값을 '결제완료'로 변경처리) 진행

        return new PaymentResponse(savedPayment); // '결제완료' 처리된 결제객체를 응답dto에 담아 반환
    }
}