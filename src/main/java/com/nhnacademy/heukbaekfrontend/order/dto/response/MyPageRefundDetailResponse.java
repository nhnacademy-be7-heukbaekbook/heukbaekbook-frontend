package com.nhnacademy.heukbaekfrontend.order.dto.response;

import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;

import java.util.List;

public record MyPageRefundDetailResponse(
        GradeDto gradeDto,
        List<RefundDetailResponse> refunds
) {}
