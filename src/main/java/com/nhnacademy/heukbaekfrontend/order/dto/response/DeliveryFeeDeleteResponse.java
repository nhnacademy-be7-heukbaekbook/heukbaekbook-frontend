package com.nhnacademy.heukbaekfrontend.order.dto.response;

import java.math.BigDecimal;

public record DeliveryFeeDeleteResponse(
        Long id,
        String name,
        BigDecimal fee,
        BigDecimal minimumOrderAmount
) {}
