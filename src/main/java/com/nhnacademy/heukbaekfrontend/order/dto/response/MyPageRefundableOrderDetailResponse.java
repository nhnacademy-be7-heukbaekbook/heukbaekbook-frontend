package com.nhnacademy.heukbaekfrontend.order.dto.response;

import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;

public record MyPageRefundableOrderDetailResponse(
        MemberResponse memberResponse,
        RefundableOrderDetailResponse order
) {}
