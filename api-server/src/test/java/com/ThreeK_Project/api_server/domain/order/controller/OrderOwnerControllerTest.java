package com.ThreeK_Project.api_server.domain.order.controller;

import com.ThreeK_Project.api_server.domain.order.enums.OrderStatus;
import com.ThreeK_Project.api_server.domain.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class OrderOwnerControllerTest {
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderOwnerController orderOwnerController;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderOwnerController).build();
    }


    @Test
    @DisplayName("주문 상태 변경 성공 테스트")
    public void updateOrderStatusTest() throws Exception {
        UUID orderId = UUID.randomUUID();
        String content = "{\"orderStatus\":\"CANCELED\"}";

        mockMvc.perform(patch("/api/owner/orders/" + orderId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("주문 상태 변경 성공"));

        verify(orderService, times(1)).updateOrderStatus(orderId, OrderStatus.CANCELED);
    }
}