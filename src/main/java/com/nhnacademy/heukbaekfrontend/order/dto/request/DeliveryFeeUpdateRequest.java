package com.nhnacademy.heukbaekfrontend.order.dto.request;

import java.math.BigDecimal;

public record DeliveryFeeUpdateRequest(
        String name,
        BigDecimal fee,
        BigDecimal minimumOrderAmount
) {}
