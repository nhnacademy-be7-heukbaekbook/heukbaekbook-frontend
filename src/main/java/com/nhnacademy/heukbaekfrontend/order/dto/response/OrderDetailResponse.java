package com.nhnacademy.heukbaekfrontend.order.dto.response;

import java.util.List;

public record OrderDetailResponse(
        String tossOrderId,
        String customerName,
        String deliveryFee,
        String paymentPrice,
        String paymentTypeName,
        String recipient,
        Long postalCode,
        String roadNameAddress,
        String detailAddress,
        String totalBookPrice,
        String totalBookDiscountPrice,
        String totalPrice,
        String status,
        List<OrderBookResponse> books) {
}