package com.nhnacademy.heukbaekfrontend.book.dto.response;

public record BookOrderResponse(Long id,
                                String title,
                                String price,
                                String salePrice,
                                double discountRate,
                                String thumbnailUrl,
                                int quantity) {
}
