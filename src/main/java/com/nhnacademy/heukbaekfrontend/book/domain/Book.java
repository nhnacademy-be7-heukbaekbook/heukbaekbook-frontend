package com.nhnacademy.heukbaekfrontend.book.domain;

import java.math.BigDecimal;

public record Book(Long id,
                   String title,
                   boolean isPackable,
                   Long wrappingPaperId,
                   String price,
                   String salePrice,
                   BigDecimal discountRate,
                   BigDecimal discountAmount,
                   String thumbnailUrl,
                   int quantity,
                   String formattedTotalPrice,
                   BigDecimal totalPrice) {
}