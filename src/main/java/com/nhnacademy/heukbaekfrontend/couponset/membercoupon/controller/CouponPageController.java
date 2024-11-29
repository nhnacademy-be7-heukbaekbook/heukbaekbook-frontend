package com.nhnacademy.heukbaekfrontend.couponset.membercoupon.controller;

import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto.CouponHistoryResponse;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto.MemberCouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.service.CouponPageService;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MyPageResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CouponPageController {
    private final CouponPageService couponPageService;
    private final MemberService memberService;

    @GetMapping("/members/mypage/coupons")
    public String showCouponsPage(Model model) {
        MyPageResponse myPageResponse = memberService.createMyPageResponse();
        model.addAttribute("gradeDto", myPageResponse.gradeDto());

        MemberResponse memberResponse = memberService.getMember().getBody();
        model.addAttribute("memberResponse", memberResponse);
        Page<MemberCouponResponse> availableCoupons = couponPageService.getAvailableCoupons();
        Page<CouponHistoryResponse> usedCoupons = couponPageService.getUsedCoupons();

        model.addAttribute("availableCoupons", availableCoupons);
        model.addAttribute("usedCoupons", usedCoupons);

        return "mypage/mypage-coupons";
    }
}


