package com.nhnacademy.heukbaekfrontend.book.domain;

import java.math.BigDecimal;

public record Book(Long id, String title, BigDecimal price, double discountRate, int quantity) {
}