package com.nhnacademy.heukbaekfrontend.order.dto.response;

import java.time.LocalDate;

public record OrderSummaryResponse(
        LocalDate createdAt,
        String tossOrderId,
        String status,
        DeliverySummaryResponse delivery
) {
}
