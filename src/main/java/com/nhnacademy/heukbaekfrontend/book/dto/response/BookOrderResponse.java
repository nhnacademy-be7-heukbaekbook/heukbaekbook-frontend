package com.nhnacademy.heukbaekfrontend.book.dto.response;

import java.math.BigDecimal;

public record BookOrderResponse(Long id,
                                String title,
                                String price,
                                String salePrice,
                                BigDecimal discountRate,
                                String thumbnailUrl,
                                int quantity) {
}
