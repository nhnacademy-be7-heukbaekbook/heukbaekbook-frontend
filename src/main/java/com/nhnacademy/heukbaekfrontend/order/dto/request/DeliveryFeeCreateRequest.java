package com.nhnacademy.heukbaekfrontend.order.dto.request;

import java.math.BigDecimal;

public record DeliveryFeeCreateRequest(
        String name,
        BigDecimal fee,
        BigDecimal minimumOrderAmount
) {}
