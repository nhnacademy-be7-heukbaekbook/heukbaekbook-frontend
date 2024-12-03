package com.nhnacademy.heukbaekfrontend.order.dto.request;

import java.math.BigDecimal;

public record RefundBookRequest(
        Long bookId,
        Integer quantity,
        BigDecimal price
) {}
