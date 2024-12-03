package com.nhnacademy.heukbaekfrontend.order.client;

import com.nhnacademy.heukbaekfrontend.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentApprovalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment", url = "http://localhost:8082/api/payments")
public interface PaymentClient {

    @PostMapping("/confirm")
    ResponseEntity<PaymentApprovalResponse> approvePayment(PaymentApprovalRequest request);
}
