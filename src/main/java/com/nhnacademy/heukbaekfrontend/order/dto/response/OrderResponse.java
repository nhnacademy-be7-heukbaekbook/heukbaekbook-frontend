package com.nhnacademy.heukbaekfrontend.order.dto.response;

import java.util.List;

public record OrderResponse(
        List<OrderSummaryResponse> orderSummaryResponseList
) {
}
