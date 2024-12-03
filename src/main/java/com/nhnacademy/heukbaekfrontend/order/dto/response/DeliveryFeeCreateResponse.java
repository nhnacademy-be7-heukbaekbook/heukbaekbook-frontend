package com.nhnacademy.heukbaekfrontend.order.dto.response;

import java.math.BigDecimal;

public record DeliveryFeeCreateResponse(
        String name,
        BigDecimal fee,
        BigDecimal minimumOrderAmount
) {}
