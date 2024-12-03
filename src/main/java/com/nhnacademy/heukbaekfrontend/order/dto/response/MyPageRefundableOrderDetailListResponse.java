package com.nhnacademy.heukbaekfrontend.order.dto.response;

import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;

import java.util.List;

public record MyPageRefundableOrderDetailListResponse(
        GradeDto gradeDto,
        List<RefundableOrderDetailResponse> orders
) {}
