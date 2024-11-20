package com.nhnacademy.heukbaekfrontend.point.controller;

import com.nhnacademy.heukbaekfrontend.common.annotation.Member;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
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

import static com.nhnacademy.heukbaekfrontend.memberset.member.controller.MemberController.MEMBER_RESPONSE;

@Controller
@RequestMapping("/members/mypage")
@RequiredArgsConstructor
public class PointHistoryController {

    private final MemberService memberService;
    private final PointHistoryService pointHistoryService;

    @Member
    @GetMapping("/points")
    public String getMyPagePoints(Model model, Pageable pageable) {
        MemberResponse memberResponse = memberService.getMember().getBody();
        BigDecimal currentPointBalance = pointHistoryService.getCurrentPointBalance();
        Page<PointHistoryResponse> pointHistories = pointHistoryService.getPointHistories(pageable);

        model.addAttribute(MEMBER_RESPONSE, memberResponse);
        model.addAttribute("balance", currentPointBalance);
        model.addAttribute("pointHistories", pointHistories);

        return "mypage/mypage-points";
    }
}
