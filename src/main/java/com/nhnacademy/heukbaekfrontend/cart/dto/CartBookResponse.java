package com.nhnacademy.heukbaekfrontend.cart.dto;

import java.math.BigDecimal;

public record CartBookResponse(
        Long id,
        String thumbnailUrl,
        String title,
        BigDecimal price,
        BigDecimal salePrice,
        BigDecimal discountRate,
        int quantity
) {
}
