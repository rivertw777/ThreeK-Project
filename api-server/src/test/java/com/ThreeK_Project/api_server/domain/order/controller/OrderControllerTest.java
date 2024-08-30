package com.ThreeK_Project.api_server.domain.order.controller;

import com.ThreeK_Project.api_server.customMockUser.WithCustomMockUser;
import com.ThreeK_Project.api_server.domain.order.dto.RequestDto.OrderSearchDTO;
import com.ThreeK_Project.api_server.domain.order.dto.ResponseDto.OrderResponseDto;
import com.ThreeK_Project.api_server.domain.order.dto.ResponseDto.ProductResponseData;
import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.order.entity.OrderProduct;
import com.ThreeK_Project.api_server.domain.order.enums.OrderStatus;
import com.ThreeK_Project.api_server.domain.order.enums.OrderType;
import com.ThreeK_Project.api_server.domain.order.service.OrderService;
import com.ThreeK_Project.api_server.domain.payment.dto.PaymentRequestDto;
import com.ThreeK_Project.api_server.domain.payment.entity.Payment;
import com.ThreeK_Project.api_server.domain.payment.enums.PaymentMethod;
import com.ThreeK_Project.api_server.domain.payment.enums.PaymentStatus;
import com.ThreeK_Project.api_server.domain.payment.service.PaymentService;
import com.ThreeK_Project.api_server.domain.product.entity.Product;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;


    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    @DisplayName("주문 취소 성공 테스트")
    @WithCustomMockUser
    public void cancelOrderTest() throws Exception {
        UUID orderId = UUID.randomUUID();
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        doNothing().when(orderService).cancelOrder(UUID.randomUUID(), user.getUsername());

        mockMvc.perform(patch("/api/orders/" + orderId +"/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("주문 취소 성공"));
    }

    @Test
    @DisplayName("주문 상태 변경 성공 테스트")
    public void updateOrderStatusTest() throws Exception {
        UUID orderId = UUID.randomUUID();
        String content = "{\"orderStatus\":\"CANCELED\"}";

        doNothing().when(orderService).updateOrderStatus(orderId, OrderStatus.CANCELED);

        mockMvc.perform(patch("/api/orders/" + orderId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("주문 상태 변경 성공"));
    }

    @Test
    @DisplayName("주문 조회 성공 태스트")
    public void getOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        Restaurant restaurant = new Restaurant();
        Order order = Order.createOrder(
                OrderType.ONLINE, OrderStatus.WAIT, new BigDecimal(10000),
                "서울", "문앞에 놓고 노크", restaurant
        );
        Product product = Product.createProduct(
            "햄버거", 5000, "햄버거 단품", restaurant
        );
        OrderProduct orderProduct = OrderProduct.createOrderProduct(
            2, new BigDecimal(10000), order, product
        );
        Payment payment = Payment.createPayment(
            PaymentMethod.CARD, PaymentStatus.SUCCESS, new BigDecimal(10000), order
        );
        OrderResponseDto responseDto = new OrderResponseDto(order);

        doReturn(responseDto)
                .when(orderService)
                .getOrder(orderId);

        mockMvc.perform(get("/api/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("orderStatus").value(OrderStatus.WAIT.toString()))
                .andExpect(jsonPath("orderType").value(OrderType.ONLINE.toString()))
                .andExpect(jsonPath("deliveryAddress").value("서울"))
                .andExpect(jsonPath("orderPayment.paymentStatus").value("SUCCESS"))
                .andExpect(jsonPath("orderedProducts[0].productName").value("햄버거"));
    }

    @Test
    @DisplayName("주문 삭제 성공 테스트")
    @WithCustomMockUser
    public void deleteOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        doNothing().when(orderService).deleteOrder(orderId, user);

        mockMvc.perform(delete("/api/orders/" + orderId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("주문 삭제 성공"));
    }

    @Test
    @DisplayName("결제 정보 생성 성공 테스트")
    public void createPaymentTest() throws Exception {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        String content = "{\"paymentStatus\":\"WAIT\",\"paymentAmount\": \"10000\"}";

        doReturn(order)
                .when(orderService)
                .findOrderById(any());

        doNothing()
                .when(paymentService)
                .createPayment(order, new PaymentRequestDto());

        mockMvc.perform(post("/api/orders/" + orderId + "/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("결제 정보 생성 성공"));
    }

}