package com.ThreeK_Project.api_server.domain.payment.controller;

import com.ThreeK_Project.api_server.customMockUser.WithCustomMockUser;
import com.ThreeK_Project.api_server.domain.payment.dto.RequestDto.PaymentUpdateDto;
import com.ThreeK_Project.api_server.domain.payment.enums.PaymentMethod;
import com.ThreeK_Project.api_server.domain.payment.enums.PaymentStatus;
import com.ThreeK_Project.api_server.domain.payment.service.PaymentService;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class PaymentAdminControllerTest {
    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentAdminController paymentAdminController;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentAdminController).build();
    }

    @Test
    @DisplayName("결제 정보 수정 성공 테스트")
    public void updatePaymentTest() throws Exception {
        UUID paymentId = UUID.randomUUID();
        PaymentUpdateDto paymentUpdateDto = new PaymentUpdateDto(
                PaymentMethod.CARD, PaymentStatus.FAIL, new BigDecimal(10000)
        );
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(paymentUpdateDto);

        mockMvc.perform(put("/api/admin/payments/" + paymentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("결제 정보 수정 성공"));
    }


    @Test
    @DisplayName("결제 정보 삭제 성공 테스트")
    @WithCustomMockUser
    public void deletePaymentTest() throws Exception {
        UUID paymentId = UUID.randomUUID();
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        mockMvc.perform(delete("/api/admin/payments/" + paymentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("결제 정보 삭제 성공"));
    }

}