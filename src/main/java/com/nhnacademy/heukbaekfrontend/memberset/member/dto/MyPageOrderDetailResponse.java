package com.nhnacademy.heukbaekfrontend.memberset.member.dto;

import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderDetailResponse;

public record MyPageOrderDetailResponse(
        MemberResponse memberResponse,
        OrderDetailResponse orderDetailResponse
) {
}
