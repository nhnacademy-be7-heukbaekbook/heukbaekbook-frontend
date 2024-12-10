package com.nhnacademy.heukbaekfrontend.order.service;

import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderUpdateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    OrderFormResponse createOrderFormResponse(String sessionId, List<Long> bookIds, Integer quantity);

    ResponseEntity<Long> createOrder(OrderCreateRequest orderCreateRequest);

    OrderDetailResponse createOrderDetailResponse(String tossOrderId);

    MyPageRefundableOrderDetailListResponse getRefundableOrders();

    MyPageRefundableOrderDetailResponse getRefundableOrderDetail(Long orderId);

    void deleteOrder(String orderId);

    OrderResponse getOrders(Pageable pageable);

    void updateOrder(String orderId, OrderUpdateRequest orderUpdateRequest);
}
