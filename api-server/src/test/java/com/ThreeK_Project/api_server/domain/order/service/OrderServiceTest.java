package com.ThreeK_Project.api_server.domain.order.service;

import com.ThreeK_Project.api_server.domain.order.dto.OrderResponseDto;
import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.order.enums.OrderStatus;
import com.ThreeK_Project.api_server.domain.order.enums.OrderType;
import com.ThreeK_Project.api_server.domain.order.repository.OrderRepository;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Order order;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문 조회 성공 테스트")
    public void getOrderTest(){
        UUID orderId = UUID.randomUUID();
        Order order = Order.createOrder(
            OrderType.ONLINE, OrderStatus.WAIT, new BigDecimal(10000),
            "서울", "문앞에 놓고 노크", new Restaurant()
        );

        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(any(UUID.class));

        OrderResponseDto orderResponseDto = orderService.getOrder(orderId);

        assertEquals(order.getOrderStatus(), orderResponseDto.getOrderStatus());
        assertEquals(order.getOrderType(), orderResponseDto.getOrderType());
        assertEquals(order.getDeliveryAddress(), orderResponseDto.getDeliveryAddress());
        assertEquals(order.getDeliveryDetails(), orderResponseDto.getDeliveryDetails());
    }

    @Test
    @DisplayName("주문 조회 실패 테스트 - 주문 기록 없음")
    public void getOrderTest2(){
        doReturn(Optional.empty())
                .when(orderRepository)
                .findById(any(UUID.class));

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.getOrder(UUID.randomUUID()));
        assertThat(e.getMessage()).isEqualTo("Order not found");
    }

    @Test
    @DisplayName("주문 삭제 성공 테스트")
    public void deleteOrderTest(){
        UUID orderId = UUID.randomUUID();

        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(any(UUID.class));

        doNothing()
                .when(order)
                .deleteBy(any(User.class));

        orderService.deleteOrder(orderId, new User());
    }

    @Test
    @DisplayName("주문 삭제 실패 테스트 - 주문 기록 없음")
    public void deleteOrderTest2(){
        UUID orderId = UUID.randomUUID();

        doReturn(Optional.empty())
                .when(orderRepository)
                .findById(any(UUID.class));

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.deleteOrder(orderId, new User()));
        assertThat(e.getMessage()).isEqualTo("Order not found");
    }
}