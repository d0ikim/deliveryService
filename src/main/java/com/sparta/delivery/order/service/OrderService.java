package com.sparta.delivery.order.service;

import com.sparta.delivery.menu.entity.Menu;
import com.sparta.delivery.menu.repository.MenuRepository;
import com.sparta.delivery.order.dto.request.OrderRequest;
import com.sparta.delivery.order.dto.response.OrderResponse;
import com.sparta.delivery.order.entity.Order;
import com.sparta.delivery.order.entity.OrderStatus;
import com.sparta.delivery.order.repository.OrderRepository;
import com.sparta.delivery.user.entity.Role;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    // 8. 주문 생성
    @Transactional
    public OrderResponse create(String username, OrderRequest request) {
        // username으로 손님 찾기
        User customer = userRepository.findByUsername(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // request의 menuId로 삭제되지않은 menu 찾기
        Menu menu = menuRepository.findByIdAndDeletedFalse(request.getMenuId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."));

        // 찾은 menu의 가격 * request의 주문수량 = 총금액
        Integer totalPrice = menu.getPrice() * request.getQuantity();   // 총액은 서버가 계산 요구사항

        // 손님객체, 메뉴객체, 주문수량, 총금액, request의 배달주소로 새 주문객체 만들기
        Order order = new Order(customer, menu, request.getQuantity(), totalPrice, request.getDeliveryAddress());
        Order savedOrder = orderRepository.save(order); // 주문테이블에 주문객체(정보) 저장한 '저장된 주문'객체 대입

        return new OrderResponse(savedOrder);   // 저장된주문객체를 orderResponse모양에 맞게 담아 반환
    }

    // 9. 주문목록 조회
    public List<OrderResponse> getOrders(String username) {
        // username으로 주문자를 찾기 (없으면 에러)
        User user = userRepository.findByUsername(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // 로그인한 사용자, 역할별로 다른 목록 요구사항
        // 주문자의 역할이 사장님이면, "내 메뉴에 들어온 주문" 전부 조회해 리스트에 저장,
        // 주문자의 역할이 사장님이 아니면(손님이면), 손님의 주문 전부 조회해 리스트에 저장
        List<Order> orders = user.getRole() == Role.OWNER
                ? orderRepository.findAllByMenuOwnerUsername(username)
                : orderRepository.findAllByCustomerUsername(username);

        return orders.stream().map(OrderResponse::new).toList();
    }

    // 10. 주문 취소
    @Transactional
    public void cancel(String username, Long orderId) {
        // 1) 주문번호로 주문객체 저장 (없으면 404) - 존재확인이 항상 가장먼저 순서!
        Order order = findOrderOrThrow(orderId);

        // 2) 해당 주문객체의 > 주문자의 > 이름이 취소자(본인)와 일치하지 않을 경우 403
        if (!order.getCustomer().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 주문만 취소할 수 있습니다.");
        }
        // 3) 해당 주문객체의 주문상태가 '주문요청'이 아니면 409
        if (order.getStatus() != OrderStatus.ORDERED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "주문요청 상태일때만 취소할 수 있습니다.");
        }
        order.cancel(); // 주문객체의 주문상태를 '주문취소'로 변경해 저장
    }

    // 11. 주문상태 변경
    @Transactional
    public OrderResponse updateStatus(String username, Long orderId) {
        // 주문번호로 주문객체 저장
        Order order = findOrderOrThrow(orderId);

        // 해당 주문객체의 > 주문된메뉴의 > 소유자(사장님)의 > 이름이 상태변경자(본인)와 일치하지 않을 경우
        if (!order.getMenu().getOwner().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 메뉴에 들어온 주문만 처리할 수 있습니다.");
        }

        // 주문객체의 주문상태가
        switch (order.getStatus()) {
            case PAID -> order.accept();        // '결제완료'일 경우, '주문수락'상태로 한 단계만 변경
            case ACCEPTED -> order.complete();  // '주문수락'일 경우, '배달완료'상태로 한 단계만 변경
            // 그 외값('주문요청'일 경우, 결제 전 주문을 허락할 수 없기에,
            // '배달완료'일 경우, 배달완료된 걸 또 바꿀 수 없기에,
            // '주문취소'일 경우, 주문취소메서드에서 따로 처리하기때문에 에러처리
            default -> throw new ResponseStatusException(HttpStatus.CONFLICT, "지금 상태에서는 변경할 수 없습니다.");
        }

        return new OrderResponse(order);
    }

    private Order findOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));
    }
}