package com.nhnacademy.heukbaekfrontend.order.service;

import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderFormResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    OrderFormResponse createOrderFormResponse(String sessionId, List<Long> bookIds, Integer quantity);

    ResponseEntity<Long> createOrder(OrderCreateRequest orderCreateRequest);

    OrderDetailResponse createOrderDetailResponse(String tossOrderId);
}
