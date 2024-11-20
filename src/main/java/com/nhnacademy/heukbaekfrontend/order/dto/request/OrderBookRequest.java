package com.nhnacademy.heukbaekfrontend.order.dto.request;

public record OrderBookRequest(Long bookId,
                               boolean isWrapped,
                               Long wrappingPaperId) {
}
