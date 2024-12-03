package com.nhnacademy.heukbaekfrontend.book.dto.redis;

import java.math.BigDecimal;

public record BookInfo(
    Long id,
    String thumbnailUrl,
    String title,
    BigDecimal price,
    BigDecimal salePrice,
    BigDecimal discountRate
) {
}
