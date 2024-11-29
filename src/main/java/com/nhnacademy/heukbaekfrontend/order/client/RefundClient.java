package com.nhnacademy.heukbaekfrontend.order.client;

import com.nhnacademy.heukbaekfrontend.order.dto.request.RefundCreateRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.MyPageRefundDetailResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.RefundCreateResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.RefundDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "refund", url = "http://localhost:8082/api/refunds")
public interface RefundClient {

    @GetMapping("/{refund-id}")
    ResponseEntity<RefundDetailResponse> getRefund(@PathVariable(name = "refund-id") Long refundId);

    @GetMapping
    ResponseEntity<MyPageRefundDetailResponse> getRefunds(@RequestParam String customerId);

    @PostMapping
    ResponseEntity<RefundCreateResponse> createRefund(@RequestBody RefundCreateRequest request);
}
