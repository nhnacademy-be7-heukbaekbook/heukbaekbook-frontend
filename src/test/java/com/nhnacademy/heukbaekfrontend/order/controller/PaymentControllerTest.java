package com.nhnacademy.heukbaekfrontend.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentApprovalResponse;
import com.nhnacademy.heukbaekfrontend.order.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CookieUtil cookieUtil;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new PaymentController(paymentService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testViewOrderSheet() throws Exception {
        mockMvc.perform(get("/payment"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/orderSheet"));
    }

    @Test
    void testViewSuccess() throws Exception {
        mockMvc.perform(get("/payment/success"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/success"));
    }

    @Test
    void testViewFail() throws Exception {
        mockMvc.perform(get("/payment/fail"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/fail"));
    }

    @Test
    void testConfirmPayment() throws Exception {
        PaymentApprovalRequest mockRequest = new PaymentApprovalRequest("order123", "user123", 10000L,"abc");
        PaymentApprovalResponse mockResponse = new PaymentApprovalResponse("결제에 성공하였습니다.");

        when(paymentService.approvePayment(any(PaymentApprovalRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/payment/confirm")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(mockRequest))) // 요청 JSON 직렬화
                .andExpect(status().isCreated()) // HTTP 상태 코드 검증
                .andExpect(jsonPath("$.message").value("결제에 성공하였습니다."));
    }

    @Test
    void testViewResult() throws Exception {
        mockMvc.perform(get("/payment/result"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("response"))
                .andExpect(view().name("order/detail"));
    }
}
