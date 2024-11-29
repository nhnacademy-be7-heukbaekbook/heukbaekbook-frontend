package com.nhnacademy.heukbaekfrontend.order.dto.response;

import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;

public record MyPageRefundableOrderDetailResponse(
        GradeDto gradeDto,
        RefundableOrderDetailResponse order
) {}
