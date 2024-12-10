package com.nhnacademy.heukbaekfrontend.order.controller;

import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderBookRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderFormResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.WrappingPaperResponse;
import com.nhnacademy.heukbaekfrontend.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser // Mock 인증 사용자 추가
    void testGetOrderForm() throws Exception {
        MemberDetailResponse memberDetailResponse = new MemberDetailResponse(
                1L, "John Doe", "010-1234-5678", "john.doe@example.com",
                BigDecimal.valueOf(10000), List.of(new MemberAddressResponse(
                1L, 12345L, "123 Main St", "Apt 101", "Home"
        )));
        OrderFormResponse orderFormResponse = new OrderFormResponse(
                memberDetailResponse,
                List.of(),
                "100000",
                "5000",
                "3000",
                "98000",
                List.of(new WrappingPaperResponse(1L, "Gift Wrap", BigDecimal.valueOf(500), "url"))
        );

        when(orderService.createOrderFormResponse(anyString(), any(), any())).thenReturn(orderFormResponse);

        mockMvc.perform(get("/order")
                        .param("bookIds", "1", "2")
                        .param("quantity", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/orderForm"))
                .andExpect(model().attributeExists("orderFormResponse"));
    }

//    @Test
//    @WithMockUser // Mock 인증 사용자 추가
//    void testGetOrderDetailForm() throws Exception {
//        OrderDetailResponse orderDetailResponse = new OrderDetailResponse(
//                "John Doe", "3000", "98000", "Credit Card", "Jane Doe", 12345L,
//                "123 Main St", "Apt 101", "100000", "2000", "98000", "Delivered",
//                List.of()
//        );
//
//        when(orderService.createOrderDetailResponse(anyString())).thenReturn(orderDetailResponse);
//
//        mockMvc.perform(get("/order/123"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("order/detail"))
//                .andExpect(model().attributeExists("orderDetailResponse"))
//                .andExpect(model().attributeExists("orderId"));
//    }

    @Test
    @WithMockUser // Mock 인증 사용자 추가
    void testCreateOrder() throws Exception {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                1L, "98000", "john.doe@example.com", "John Doe", "010-1234-5678",
                "TOSS123", "Jane Doe", "12345", "123 Main St", "Apt 101",
                "010-8765-4321", "3000", "1000",
                List.of(new OrderBookRequest(1L, 2, "45000", true, 1L))
        );

        when(orderService.createOrder(any(OrderCreateRequest.class))).thenReturn(ResponseEntity.ok(123L));

        mockMvc.perform(post("/order")
                        .with(csrf()) // CSRF 토큰 추가
                        .contentType("application/json")
                        .content("""
                                {
                                    "customerId": 1,
                                    "totalPrice": "98000",
                                    "customerEmail": "john.doe@example.com",
                                    "customerName": "John Doe",
                                    "customerPhoneNumber": "010-1234-5678",
                                    "tossOrderId": "TOSS123",
                                    "recipient": "Jane Doe",
                                    "postalCode": "12345",
                                    "roadNameAddress": "123 Main St",
                                    "detailAddress": "Apt 101",
                                    "recipientPhoneNumber": "010-8765-4321",
                                    "deliveryFee": "3000",
                                    "usedPoint": "1000",
                                    "orderBookRequests": [
                                        {
                                            "bookId": 1,
                                            "quantity": 2,
                                            "salePrice": "45000",
                                            "isWrapped": true,
                                            "wrappingPaperId": 1
                                        }
                                    ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("123"));
    }
}
