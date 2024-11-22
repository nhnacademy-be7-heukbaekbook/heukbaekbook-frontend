package com.nhnacademy.heukbaekfrontend.order.dto.request;

public record OrderBookRequest(Long bookId,
                               int quantity,
                               String salePrice,
                               boolean isWrapped,
                               Long wrappingPaperId) {
}
