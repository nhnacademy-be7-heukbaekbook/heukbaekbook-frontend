package com.nhnacademy.heukbaekfrontend.order.dto.request;

import java.util.List;

public record RefundCreateRequest(
        List<RefundBookRequest> refundBooks,
        String paymentKey,
        String cancelReason,
        String method
) {}
