package com.nhnacademy.heukbaekfrontend.order.client;

import com.nhnacademy.heukbaekfrontend.order.dto.request.OrderCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "orderClient", url = "http://localhost:8082/api/orders")
public interface OrderClient {

    @PostMapping
    ResponseEntity<Long> createOrder(OrderCreateRequest orderCreateRequest);
}
