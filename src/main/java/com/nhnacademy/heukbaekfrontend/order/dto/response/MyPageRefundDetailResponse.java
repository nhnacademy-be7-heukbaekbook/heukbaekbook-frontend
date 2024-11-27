package com.nhnacademy.heukbaekfrontend.order.dto.response;

import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;

import java.util.List;

public record MyPageRefundDetailResponse(
        MemberResponse memberResponse,
        List<RefundDetailResponse> refunds
) {}
