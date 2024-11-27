package com.nhnacademy.heukbaekfrontend.order.dto.response;

import java.math.BigDecimal;

public record WrappingPaperResponse(
        Long id,
        String name,
        BigDecimal price,
        String imageUrl
) {
}
