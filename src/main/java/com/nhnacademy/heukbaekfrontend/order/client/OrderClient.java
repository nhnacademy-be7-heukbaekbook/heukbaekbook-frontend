package com.nhnacademy.heukbaekfrontend.order.client;

import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderUpdateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.MyPageRefundableOrderDetailListResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.MyPageRefundableOrderDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderResponse;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "orderClient", url = "http://localhost:8082/api/orders")
public interface OrderClient {

    @PostMapping
    ResponseEntity<Long> createOrder(OrderCreateRequest orderCreateRequest);

    @GetMapping("/{orderId}")
    OrderDetailResponse getOrderDetailResponse(@PathVariable String orderId);

    @GetMapping("/refundable-orders")
    ResponseEntity<MyPageRefundableOrderDetailListResponse> getRefundableOrders(@RequestParam(name = "customer-id") String customerId);

    @GetMapping("/refundable-orders/{order-id}")
    ResponseEntity<MyPageRefundableOrderDetailResponse> getRefundableOrderDetail(
            @RequestParam(name = "customer-id") String customerId,
            @PathVariable(name = "order-id") Long orderId
    );

    @DeleteMapping("/{orderId}")
    void deleteOrder(@PathVariable String orderId);

    @GetMapping
    OrderResponse getOrders(Pageable pageable);

    @PutMapping("/{orderId}")
    void updateOrder(@PathVariable String orderId,
                     @RequestBody OrderUpdateRequest orderUpdateRequest);
}
