package com.nhnacademy.heukbaekfrontend.memberset.member.dto;

import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderResponse;

public record MyPageOrderListResponse(
        MemberResponse memberResponse,
        OrderResponse orderResponse
) {
}
