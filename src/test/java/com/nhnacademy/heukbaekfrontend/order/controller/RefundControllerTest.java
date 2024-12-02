package com.nhnacademy.heukbaekfrontend.order.controller;

import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekfrontend.order.dto.request.RefundBookRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.RefundCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.*;
import com.nhnacademy.heukbaekfrontend.order.service.OrderService;
import com.nhnacademy.heukbaekfrontend.order.service.RefundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RefundController.class)
class RefundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RefundService refundService;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new RefundController(refundService, orderService)).build();
    }

    @Test
    void testGetAllRefunds() throws Exception {
        GradeDto gradeDto = new GradeDto("Gold", BigDecimal.valueOf(0.05), BigDecimal.valueOf(100000));
        RefundDetailResponse refundDetail = new RefundDetailResponse(
                1L, "Damaged book", "2023-12-01T10:00:00", "2023-12-02T10:00:00",
                "APPROVED", 101L, List.of("Book Title"), List.of(1), List.of(BigDecimal.valueOf(15000))
        );
        MyPageRefundDetailResponse response = new MyPageRefundDetailResponse(gradeDto, List.of(refundDetail));

        when(refundService.getAllRefunds()).thenReturn(response);

        mockMvc.perform(get("/refunds"))
                .andExpect(status().isOk())
                .andExpect(view().name("refund/viewAllRefunds"))
                .andExpect(model().attributeExists("gradeDto"))
                .andExpect(model().attributeExists("refunds"));
    }

    @Test
    void testGetRefundableOrders() throws Exception {
        GradeDto gradeDto = new GradeDto("Gold", BigDecimal.valueOf(0.05), BigDecimal.valueOf(100000));
        RefundableOrderDetailResponse orderDetail = new RefundableOrderDetailResponse(
                101L, "John Doe", "3000", "153000", "Credit Card", "Jane Doe", 12345L,
                "123 Main St", "Apt 4B", "150000", "3000", "153000",
                List.of(new RefundableOrderBookResponse(1L, "url", "Book Title", "15000", 1, "14500", BigDecimal.valueOf(0.05), "14500")),
                null, "DELIVERED", "PAY12345"
        );
        MyPageRefundableOrderDetailListResponse response = new MyPageRefundableOrderDetailListResponse(gradeDto, List.of(orderDetail));

        when(orderService.getRefundableOrders()).thenReturn(response);

        mockMvc.perform(get("/refunds/refundable-orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("refund/refundableOrders"))
                .andExpect(model().attributeExists("gradeDto"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    void testGetRefundableOrderDetail() throws Exception {
        RefundableOrderDetailResponse orderDetail = new RefundableOrderDetailResponse(
                101L, "John Doe", "3000", "153000", "Credit Card", "Jane Doe", 12345L,
                "123 Main St", "Apt 4B", "150000", "3000", "153000",
                List.of(new RefundableOrderBookResponse(1L, "url", "Book Title", "15000", 1, "14500", BigDecimal.valueOf(0.05), "14500")),
                null, "DELIVERED", "PAY12345"
        );
        MyPageRefundableOrderDetailResponse response = new MyPageRefundableOrderDetailResponse(
                new GradeDto("Gold", BigDecimal.valueOf(0.05), BigDecimal.valueOf(100000)), orderDetail
        );

        when(orderService.getRefundableOrderDetail(anyLong())).thenReturn(response);

        mockMvc.perform(get("/refunds/refundable-orders/101"))
                .andExpect(status().isOk())
                .andExpect(view().name("refund/refundableOrderDetail"))
                .andExpect(model().attributeExists("gradeDto"))
                .andExpect(model().attributeExists("orderDetail"))
                .andExpect(model().attributeExists("refundCreateRequest"));
    }

}
