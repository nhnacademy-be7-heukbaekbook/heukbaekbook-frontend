package com.nhnacademy.heukbaekfrontend.memberset.member.dto;

import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderResponse;

public record MyPageResponse(
        GradeDto gradeDto,
        OrderResponse orderResponse
) {
}
