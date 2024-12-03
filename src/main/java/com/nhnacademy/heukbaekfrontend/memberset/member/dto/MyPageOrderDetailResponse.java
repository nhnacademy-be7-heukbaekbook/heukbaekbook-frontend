package com.nhnacademy.heukbaekfrontend.memberset.member.dto;

import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekfrontend.order.dto.response.OrderDetailResponse;

public record MyPageOrderDetailResponse(
        GradeDto gradeDto,
        OrderDetailResponse orderDetailResponse
) {
}
