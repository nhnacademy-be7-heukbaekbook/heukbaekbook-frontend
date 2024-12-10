package com.nhnacademy.heukbaekfrontend.point.controller;

import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import com.nhnacademy.heukbaekfrontend.point.dto.PointHistoryResponse;
import com.nhnacademy.heukbaekfrontend.point.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
@RequestMapping("/members/mypage")
@RequiredArgsConstructor
public class PointHistoryController {

    private final MemberService memberService;
    private final PointHistoryService pointHistoryService;

    @GetMapping("/points")
    public String getMyPagePoints(Model model, Pageable pageable) {
        GradeDto gradeDto = memberService.getMembersGrade().get();
        BigDecimal currentPointBalance = pointHistoryService.getCurrentPointBalance();
        Page<PointHistoryResponse> pointHistories = pointHistoryService.getPointHistories(pageable);

        model.addAttribute("gradeDto", gradeDto);
        model.addAttribute("balance", currentPointBalance);
        model.addAttribute("pointHistories", pointHistories);

        return "mypage/mypage-points";
    }
}
