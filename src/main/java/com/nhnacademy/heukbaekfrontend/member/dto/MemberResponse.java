package com.nhnacademy.heukbaekfrontend.member.dto;


import com.nhnacademy.heukbaekfrontend.member.grade.dto.GradeDto;

import java.sql.Date;
import java.time.LocalDateTime;

public record MemberResponse(
        String name,
        String phoneNumber,
        String email,
        String loginId,
        Date birth,
        LocalDateTime createdAt,
        LocalDateTime lastLoginAt,
        MemberStatus memberStatus,
        GradeDto grade
) {
}
