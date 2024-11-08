package com.nhnacademy.heukbaekfrontend.book.domain;

import java.math.BigDecimal;

public record Book(Long id, String title, String price, String salePrice, double discountRate, String thumbnailUrl, int quantity, String totalPrice) {
}