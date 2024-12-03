package com.nhnacademy.heukbaekfrontend.book.dto.response;

import java.math.BigDecimal;

public record BookSummaryResponse(
        Long id,
        String title,
        boolean isPackable,
        Long wrappingPaperId,
        BigDecimal price,
        BigDecimal salePrice,
        BigDecimal discountRate,
        String thumbnailUrl) {}
