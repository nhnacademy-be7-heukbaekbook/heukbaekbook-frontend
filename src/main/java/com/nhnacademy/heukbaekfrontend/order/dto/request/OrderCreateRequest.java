package com.nhnacademy.heukbaekfrontend.order.dto.request;

import java.util.List;

public record OrderCreateRequest(String totalPrice,
                                 String customerEmail,
                                 String customerName,
                                 String customerPhoneNumber,
                                 String tossOrderId,
                                 String recipient,
                                 String postalCode,
                                 String roadNameAddress,
                                 String detailAddress,
                                 String recipientPhoneNumber,
                                 String deliveryFee,
                                 List<OrderBookRequest> orderBookRequests) {
}