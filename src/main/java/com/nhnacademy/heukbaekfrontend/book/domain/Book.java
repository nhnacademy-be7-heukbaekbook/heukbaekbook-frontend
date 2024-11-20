package com.nhnacademy.heukbaekfrontend.book.domain;

import java.math.BigDecimal;

public record Book(Long id,
                   String title,
                   boolean isPackable,
                   String price,
                   String salePrice,
                   double discountRate,
                   BigDecimal discountAmount,
                   String thumbnailUrl,
                   int quantity,
                   String formattedTotalPrice,
                   BigDecimal totalPrice) {
}