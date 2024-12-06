package com.nhnacademy.heukbaekfrontend.memberset.member.client;

import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "customerClient", url = "http://localhost:8082/api/customers")
public interface CustomerClient {

    @GetMapping("/orders/{orderId}")
    OrderDetailResponse getOrderDetailResponse(@PathVariable String orderId,
                                               @RequestParam String email);
}
