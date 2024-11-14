package com.nhnacademy.heukbaekfrontend.order.dto.response;

public record PaymentApprovalResponse(
        String code,
        boolean success,
        String message
) {}
