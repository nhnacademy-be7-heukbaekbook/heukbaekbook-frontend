package com.nhnacademy.heukbaekfrontend.book.dto.response;

import java.math.BigDecimal;

public record BookSummaryResponse(Long id, String title, BigDecimal price, BigDecimal salePrice, double discountRate, String thumbnailUrl) {}
