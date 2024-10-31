package com.nhnacademy.heukbaekfrontend.book.dto;

import java.math.BigDecimal;

public record BookSummaryResponse(Long id, String title, BigDecimal price, double discountRate) {
}
