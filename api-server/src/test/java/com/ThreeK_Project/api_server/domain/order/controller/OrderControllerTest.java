package com.ThreeK_Project.api_server.domain.order.controller;

import com.ThreeK_Project.api_server.customMockUser.WithCustomMockUser;
import com.ThreeK_Project.api_server.domain.order.dto.OrderResponseDto;
import com.ThreeK_Project.api_server.domain.order.dto.ProductResponseData;
import com.ThreeK_Project.api_server.domain.order.enums.OrderStatus;
import com.ThreeK_Project.api_server.domain.order.enums.OrderType;
import com.ThreeK_Project.api_server.domain.order.service.OrderService;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;


    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    @DisplayName("주문 조회 성공 태스트")
    public void getOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        List<ProductResponseData> orderedProductList = new ArrayList<>();
        orderedProductList.add(new ProductResponseData(UUID.randomUUID(), "product", 2, new BigDecimal(5000)));
        OrderResponseDto responseDto = new OrderResponseDto(
                orderId, OrderStatus.WAIT, OrderType.ONLINE, new BigDecimal(10000),
                "서울시", "문앞에 두고 노크",  orderedProductList
        );

        doReturn(responseDto).when(orderService).getOrder(orderId);

        mockMvc.perform(get("/api/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("orderId").value(orderId.toString()))
                .andExpect(jsonPath("orderStatus").value(OrderStatus.WAIT.toString()))
                .andExpect(jsonPath("orderType").value(OrderType.ONLINE.toString()))
                .andExpect(jsonPath("deliveryAddress").value("서울시"));
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
    @DisplayName("주문 취소 성공 테스트")
    @WithCustomMockUser
    public void cancelOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        doNothing().when(orderService).cancelOrder(UUID.randomUUID(), user.getUsername());

        mockMvc.perform(patch("/api/orders/" + orderId +"/cancel"))
                .andExpect(status().isOk());
    }
}