//package com.nhnacademy.heukbaekfrontend.order.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.heukbaekfrontend.book.domain.Book;
//import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderBookRequest;
//import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderCreateRequest;
//import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderBookResponse;
//import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderDetailResponse;
//import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderFormResponse;
//import com.nhnacademy.heukbaekfrontend.order.service.OrderService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(OrderController.class)
//class OrderControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private OrderService orderService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService))
//                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
//                .build();
//    }
//
//    @Test
//    void testGetOrderForm() throws Exception {
//        // Mock Book 객체 생성
//        Book book1 = new Book(
//                1L, "Book 1", true, "10000", "9000",
//                new BigDecimal("0.10"), new BigDecimal("1000"),
//                "thumbnail1.png", 2, "18000", new BigDecimal("18000")
//        );
//        Book book2 = new Book(
//                2L, "Book 2", false, "15000", "13500",
//                new BigDecimal("0.10"), new BigDecimal("1500"),
//                "thumbnail2.png", 3, "40500", new BigDecimal("40500")
//        );
//
//        // Mock OrderFormResponse 생성
//        OrderFormResponse mockResponse = new OrderFormResponse(
//
//                List.of(book1, book2),
//                "25000",
//                "2500",
//                "3000",
//                "28000"
//        );
//
//        // Mock OrderService 설정
//        when(orderService.createOrderFormResponse(any(String.class), eq(List.of(1L, 2L)), eq(2)))
//                .thenReturn(mockResponse);
//
//        // 테스트 수행
//        mockMvc.perform(get("/order")
//                        .param("bookIds", "1", "2")
//                        .param("quantity", "2")
//                        .sessionAttr("sessionId", "test-session"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("order/orderForm"))
//                .andExpect(model().attributeExists("orderFormResponse"))
//                .andExpect(model().attribute("orderFormResponse", mockResponse));
//    }
//
//    @Test
//    void testGetOrderDetailForm() throws Exception {
//        // Mock OrderBookResponse 데이터 생성
//        OrderBookResponse book1 = new OrderBookResponse(
//                "thumbnail1.png", "Book 1", "10000", 2, "9000",
//                new BigDecimal("0.10"), "18000"
//        );
//        OrderBookResponse book2 = new OrderBookResponse(
//                "thumbnail2.png", "Book 2", "15000", 3, "13500",
//                new BigDecimal("0.10"), "40500"
//        );
//
//        // Mock OrderDetailResponse 데이터 생성
//        OrderDetailResponse mockResponse = new OrderDetailResponse(
//                "John Doe",
//                "3000",
//                "70500",
//                "Credit Card",
//                "Jane Doe",
//                123456L,
//                "123 Main St",
//                "Apt 101",
//                "25000",
//                "2500",
//                "28000",
//                List.of(book1, book2)
//        );
//
//        // Mock OrderService 동작 설정
//        when(orderService.createOrderDetailResponse(anyString())).thenReturn(mockResponse);
//
//        // 테스트 수행
//        mockMvc.perform(get("/order/1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("order/detail"))
//                .andExpect(model().attributeExists("orderDetailResponse"))
//                .andExpect(model().attribute("orderDetailResponse", mockResponse))
//                .andExpect(model().attribute("orderId", "1"));
//    }
//
//    @Test
//    void testCreateOrder() throws Exception {
//        // Mock OrderBookRequest 데이터 생성
//        OrderBookRequest bookRequest1 = new OrderBookRequest(1L, 2, "9000", true, 101L);
//        OrderBookRequest bookRequest2 = new OrderBookRequest(2L, 3, "13500", false, null);
//
//        // Mock OrderCreateRequest 데이터 생성
//        OrderCreateRequest request = new OrderCreateRequest(
//                "28000",
//                "customer@example.com",
//                "John Doe",
//                "123-456-7890",
//                "tossOrder123",
//                "Jane Doe",
//                "12345",
//                "123 Main St",
//                "Apt 101",
//                "987-654-3210",
//                "3000",
//                List.of(bookRequest1, bookRequest2)
//        );
//
//        // Mock 반환값 설정
//        Long mockOrderId = 1L;
//        when(orderService.createOrder(any(OrderCreateRequest.class))).thenReturn(ResponseEntity.ok(mockOrderId));
//
//        // 테스트 수행
//        mockMvc.perform(post("/order")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(content().string(mockOrderId.toString()));
//    }
//}
