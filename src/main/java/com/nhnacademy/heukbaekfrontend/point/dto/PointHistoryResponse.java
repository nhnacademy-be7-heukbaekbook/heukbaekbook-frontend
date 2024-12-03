package com.nhnacademy.heukbaekfrontend.point.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PointHistoryResponse(
        Long id,
        Long customerId,
        Long orderId,
        String name,
        BigDecimal amount,
        LocalDateTime createdAt,
        BigDecimal balance,
        PointType type
) {
}
