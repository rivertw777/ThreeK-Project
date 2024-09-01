package com.ThreeK_Project.api_server.domain.order.service;

import com.ThreeK_Project.api_server.domain.order.dto.RequestDto.OrderRequestDto;
import com.ThreeK_Project.api_server.domain.order.dto.RequestDto.OrderSearchDTO;
import com.ThreeK_Project.api_server.domain.order.dto.ResponseDto.OrderResponseDto;
import com.ThreeK_Project.api_server.domain.order.dto.RequestDto.ProductRequestData;
import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.order.entity.OrderProduct;
import com.ThreeK_Project.api_server.domain.order.enums.OrderStatus;
import com.ThreeK_Project.api_server.domain.order.enums.OrderType;
import com.ThreeK_Project.api_server.domain.order.repository.OrderProductRepository;
import com.ThreeK_Project.api_server.domain.order.repository.OrderRepository;
import com.ThreeK_Project.api_server.domain.payment.service.PaymentService;
import com.ThreeK_Project.api_server.domain.product.entity.Product;
import com.ThreeK_Project.api_server.domain.product.service.ProductService;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.domain.user.enums.Role;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private ProductService productService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private Order order;

    @Mock
    private User user;

    @InjectMocks
    private OrderService orderService;


    @Test
    @DisplayName("주문 생성 성공 테스트")
    public void createOrderTest1(){
        List<ProductRequestData> products = new ArrayList<>();
        products.add(new ProductRequestData(UUID.randomUUID(), 2, new BigDecimal(5000)));
        OrderRequestDto requestDto = new OrderRequestDto(
                new BigDecimal(10000), "서울시", "문앞에 두고 노크",
                OrderType.ONLINE, products
        );
        Restaurant restaurant = new Restaurant();

        doReturn(new Order())
                .when(orderRepository)
                .save(any(Order.class));

        doReturn(new Product())
                .when(productService)
                .getProductById(any(UUID.class));

        doReturn(new OrderProduct())
                .when(orderProductRepository)
                .save(any(OrderProduct.class));

        orderService.createOrder(requestDto, restaurant);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productService, times(1)).getProductById(any(UUID.class));
        verify(orderProductRepository, times(1)).save(any(OrderProduct.class));
    }

    @Test
    @DisplayName("주문 생성 실패 테스트 - 존재하지 않는 상품")
    public void createOrderTest4(){
        List<ProductRequestData> products = new ArrayList<>();
        products.add(new ProductRequestData(UUID.randomUUID(), 2, new BigDecimal(5000)));
        OrderRequestDto requestDto = new OrderRequestDto(
                new BigDecimal(10000), "서울시", "문앞에 두고 노크",
                OrderType.ONLINE, products
        );
        Restaurant restaurant = new Restaurant();

        doReturn(new Order())
                .when(orderRepository)
                .save(any(Order.class));

        doThrow(new ApplicationException("상품을 찾을 수 없습니다."))
                .when(productService)
                .getProductById(any(UUID.class));

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.createOrder(requestDto, restaurant));
        assertThat(e.getMessage()).isEqualTo("상품을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("주문 취소 성공 테스트")
    public void cancelOrderTest1(){
        UUID orderId = UUID.randomUUID();
        User user = User.createUser(
                "test", "000000", Role.CUSTOMER,
                "00000000000", "address"
        );

        doReturn(Optional.of(order))
                .when(orderRepository)
                .findOrderByIdWithPayment(any(UUID.class));

        doReturn(user)
                .when(order)
                .getCreatedBy();

        doReturn(OrderStatus.WAIT)
                .when(order)
                .getOrderStatus();

        doReturn(LocalDateTime.now())
                .when(order)
                .getCreatedAt();

        orderService.cancelOrder(orderId, "test");
    }

    @Test
    @DisplayName("주문 취소 실패 테스트 - 주문 기록 없음")
    public void cancelOrderTest2(){
        UUID orderId = UUID.randomUUID();

        doReturn(Optional.empty())
                .when(orderRepository)
                .findOrderByIdWithPayment(any(UUID.class));

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.cancelOrder(orderId, "test"));
        assertThat(e.getMessage()).isEqualTo("Order not found");
    }

    @Test
    @DisplayName("주문 취소 실패 테스트 - 본인 주문이 아님")
    public void cancelOrderTest3(){
        UUID orderId = UUID.randomUUID();
        User user = User.createUser(
                "test", "000000", Role.CUSTOMER,
                "00000000000", "address"
        );

        doReturn(Optional.of(order))
                .when(orderRepository)
                .findOrderByIdWithPayment(any(UUID.class));

        doReturn(user)
                .when(order)
                .getCreatedBy();

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.cancelOrder(orderId, "test2"));
        assertThat(e.getMessage()).isEqualTo("Invalid user");
    }

    @Test
    @DisplayName("주문 취소 실패 테스트 - 취소 불가능한 상태(WAIT x)")
    public void cancelOrderTest4(){
        UUID orderId = UUID.randomUUID();
        User user = User.createUser(
                "test", "000000", Role.CUSTOMER,
                "00000000000", "address"
        );

        doReturn(Optional.of(order))
                .when(orderRepository)
                .findOrderByIdWithPayment(any(UUID.class));

        doReturn(user)
                .when(order)
                .getCreatedBy();

        doReturn(OrderStatus.CANCELED)
                .when(order)
                .getOrderStatus();

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.cancelOrder(orderId, "test"));
        assertThat(e.getMessage()).isEqualTo("Cannot cancel");
    }

    @Test
    @DisplayName("주문 취소 실패 테스트 - 취소 시간 초과")
    public void cancelOrderTest5(){
        UUID orderId = UUID.randomUUID();
        User user = User.createUser(
                "test", "000000", Role.CUSTOMER,
                "00000000000", "address"
        );

        doReturn(Optional.of(order))
                .when(orderRepository)
                .findOrderByIdWithPayment(any(UUID.class));

        doReturn(user)
                .when(order)
                .getCreatedBy();

        doReturn(OrderStatus.WAIT)
                .when(order)
                .getOrderStatus();

        doReturn(LocalDateTime.now().minusMinutes(5))
                .when(order)
                .getCreatedAt();

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.cancelOrder(orderId, "test"));
        assertThat(e.getMessage()).isEqualTo("Cancel Timeout");
    }

    @Test
    @DisplayName("음식점 사장 주문상태 변경 성공 테스트 - WAIT > CANCELED")
    public void changeOrderStatusTest(){
        UUID orderId = UUID.randomUUID();

        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(any(UUID.class));

        doReturn(OrderStatus.WAIT)
                .when(order)
                .getOrderStatus();

        orderService.updateOrderStatus(orderId, OrderStatus.CANCELED);
    }

    @Test
    @DisplayName("음식점 사장 주문상태 변경 성공 테스트 - WAIT > RECEIPT")
    public void changeOrderStatusTest2(){
        UUID orderId = UUID.randomUUID();

        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(any(UUID.class));

        doReturn(OrderStatus.WAIT)
                .when(order)
                .getOrderStatus();

        orderService.updateOrderStatus(orderId, OrderStatus.RECEIPT);
    }

    @Test
    @DisplayName("음식점 사장 주문상태 변경 성공 테스트 - RECEIPT > COMPLETE")
    public void changeOrderStatusTest3(){
        UUID orderId = UUID.randomUUID();

        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(any(UUID.class));

        doReturn(OrderStatus.RECEIPT)
                .when(order)
                .getOrderStatus();

        orderService.updateOrderStatus(orderId, OrderStatus.COMPLETE);
    }

    @Test
    @DisplayName("음식점 사장 주문상태 변경 실패 테스트 - WAIT로 변경")
    public void changeOrderStatusTest4(){
        UUID orderId = UUID.randomUUID();

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.updateOrderStatus(orderId, OrderStatus.WAIT));
        assertThat(e.getMessage()).isEqualTo("Invalid order status");
    }

    @Test
    @DisplayName("음식점 사장 주문상태 변경 실패 테스트 - 존재하지 않는 음식점")
    public void changeOrderStatusTest5(){
        UUID orderId = UUID.randomUUID();

        doReturn(Optional.empty())
                .when(orderRepository)
                .findById(orderId);

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.updateOrderStatus(orderId, OrderStatus.RECEIPT));
        assertThat(e.getMessage()).isEqualTo("Order not found");
    }

    @Test
    @DisplayName("음식점 사장 주문상태 변경 실패 테스트 - WAIT > COMPLETE")
    public void changeOrderStatusTest6(){
        UUID orderId = UUID.randomUUID();

        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(any(UUID.class));

        doReturn(OrderStatus.WAIT)
                .when(order)
                .getOrderStatus();

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.updateOrderStatus(orderId, OrderStatus.COMPLETE));
        assertThat(e.getMessage()).isEqualTo("Invalid order status");
    }

    @Test
    @DisplayName("음식점 사장 주문상태 변경 실패 테스트 - CANCLED > anything")
    public void changeOrderStatusTest7(){
        UUID orderId = UUID.randomUUID();

        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(any(UUID.class));

        doReturn(OrderStatus.CANCELED)
                .when(order)
                .getOrderStatus();

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.updateOrderStatus(orderId, OrderStatus.COMPLETE));
        assertThat(e.getMessage()).isEqualTo("Invalid order status");
    }

    @Test
    @DisplayName("음식점 사장 주문상태 변경 실패 테스트 - COMPLETE > anything")
    public void changeOrderStatusTest9(){
        UUID orderId = UUID.randomUUID();

        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(any(UUID.class));

        doReturn(OrderStatus.COMPLETE)
                .when(order)
                .getOrderStatus();

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.updateOrderStatus(orderId, OrderStatus.COMPLETE));
        assertThat(e.getMessage()).isEqualTo("Invalid order status");
    }

    @Test
    @DisplayName("음식점 사장 주문상태 변경 실패 테스트 - RECEIPT > CANCELED")
    public void changeOrderStatusTest10(){
        UUID orderId = UUID.randomUUID();

        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(any(UUID.class));

        doReturn(OrderStatus.RECEIPT)
                .when(order)
                .getOrderStatus();

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.updateOrderStatus(orderId, OrderStatus.CANCELED));
        assertThat(e.getMessage()).isEqualTo("Invalid order status");
    }

    @Test
    @DisplayName("주문 조회 성공 테스트")
    public void getOrderTest1(){
        UUID orderId = UUID.randomUUID();
        Order order = Order.createOrder(
                OrderType.ONLINE, OrderStatus.WAIT, new BigDecimal(10000),
                "서울", "문앞에 놓고 노크", new Restaurant()
        );
        List<Role> roles = new ArrayList<>();
        roles.add(Role.MANAGER);

        doReturn(roles)
                .when(user)
                .getRoles();

        doReturn(Optional.of(order))
                .when(orderRepository)
                .findByIdWithProductsAndPayment(orderId);

        OrderResponseDto orderResponseDto = orderService.getOrder(user, orderId);
        assertEquals(order.getOrderStatus(), orderResponseDto.getOrderStatus());
        assertEquals(order.getOrderType(), orderResponseDto.getOrderType());
        assertEquals(order.getDeliveryAddress(), orderResponseDto.getDeliveryAddress());
        assertEquals(order.getDeliveryDetails(), orderResponseDto.getDeliveryDetails());
    }

    @Test
    @DisplayName("주문 조회 실패 테스트 - 주문 기록 없음")
    public void getOrderTest2(){
        UUID orderId = UUID.randomUUID();
        User user = new User();

        doReturn(Optional.empty())
                .when(orderRepository)
                .findByIdWithProductsAndPayment(any(UUID.class));

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.getOrder(user, orderId));
        assertThat(e.getMessage()).isEqualTo("Order not found");
    }

    @Test
    @DisplayName("사용자 주문 검색 성공 테스트")
    public void searchUserOrdersTest(){
        String username = "test";
        OrderSearchDTO orderSearchDTO = new OrderSearchDTO();
        orderSearchDTO.setUsername(username);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Order> orders = new ArrayList<>();
        Page<Order> pages = new PageImpl<>(orders, pageable, 0);

        doReturn(pages)
                .when(orderRepository)
                .searchOrders(pageable, orderSearchDTO);

        Page<OrderResponseDto> result = orderService.searchUserOrders(username, orderSearchDTO);
        assertEquals(result.getTotalElements(), 0);
    }

    @Test
    @DisplayName("사용자 주문 검색 실패 테스트 - 다른 사용자 정보 조회")
    public void searchUserOrdersTest2(){
        String username = "test1";
        OrderSearchDTO orderSearchDTO = new OrderSearchDTO();
        orderSearchDTO.setUsername("test2");

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.searchUserOrders(username, orderSearchDTO));
        assertThat(e.getMessage()).isEqualTo("Invalid user");
    }

    @Test
    @DisplayName("가게 주인 주문 검색 실패 테스트 - 다른 사용자 정보 조회")
    public void searchRestaurantOrdersTest2(){
        String restaurantName = "restaurant1";
        OrderSearchDTO orderSearchDTO = new OrderSearchDTO();
        orderSearchDTO.setUsername("restaurant2");

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> orderService.searchUserOrders(restaurantName, orderSearchDTO));
        assertThat(e.getMessage()).isEqualTo("Invalid user");
    }

    @Test
    @DisplayName("주문 삭제 성공 테스트")
    public void deleteOrderTest1(){
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