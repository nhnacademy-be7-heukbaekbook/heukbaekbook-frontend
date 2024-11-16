package com.nhnacademy.heukbaekfrontend.order.client;

import com.nhnacademy.heukbaekfrontend.order.dto.request.PaymentApprovalRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.request.PaymentCancelRequest;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentApprovalResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentCancelResponse;
import com.nhnacademy.heukbaekfrontend.order.dto.response.PaymentDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "toss", url = "http://localhost:8082")
public interface TossClient {

    @PostMapping("/api/payments/confirm")
    ResponseEntity<PaymentApprovalResponse> approvePayment(PaymentApprovalRequest request);

    @PostMapping("/api/payments/{payment-key}/cancel")
    ResponseEntity<PaymentCancelResponse> cancelPayment(
            @PathVariable(name = "payment-key") String paymentKey,
            PaymentCancelRequest request);

    @GetMapping("/api/payments/{payment-id}")
    ResponseEntity<PaymentDetailResponse> getPayment(@PathVariable(name = "payment-id") Long paymentId);

    @GetMapping("/api/payments/{customer-id}")
    ResponseEntity<List<PaymentDetailResponse>> getPayments(@PathVariable(name = "customer-id") Long customerId);

}
