package com.nhnacademy.heukbaekfrontend.order.dto.response;

import java.time.LocalDate;

public record OrderSummaryResponse(
        LocalDate createdAt,
        String tossOrderId,
        String title,
        String status,
        String customerName,
        String priceAndQuantity,
        DeliverySummaryResponse delivery
) {
}
