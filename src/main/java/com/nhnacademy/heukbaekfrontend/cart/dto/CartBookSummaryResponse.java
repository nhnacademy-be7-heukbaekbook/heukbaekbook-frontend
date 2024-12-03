package com.nhnacademy.heukbaekfrontend.cart.dto;

public record CartBookSummaryResponse(
        Long bookId,
        int quantity
) {
}
