package com.nhnacademy.heukbaekfrontend.order.dto.response;

public record PaymentDetailResponse(
        String paymentType,
        String requestedAt,
        String approvedAt,
        Long amount
) {}
