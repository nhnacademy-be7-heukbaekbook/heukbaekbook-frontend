package com.nhnacademy.heukbaekfrontend.order.dto.request;

public record PaymentApprovalRequest(
        String paymentKey,
        String orderId,
        Long amount
) {}
