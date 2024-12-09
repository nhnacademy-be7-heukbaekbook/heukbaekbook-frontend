package com.nhnacademy.heukbaekfrontend.couponset.membercoupon.controller;

import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto.CouponHistoryResponse;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto.MemberCouponResponse;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.service.CouponPageService;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MyPageResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CouponPageController {
    private final CouponPageService couponPageService;
    private final MemberService memberService;

    @GetMapping("/members/mypage/coupons")
    public String showCouponsPage(Model model,
                                  @PageableDefault Pageable pageable) {
        log.info("pageable: {}", pageable);
        MyPageResponse myPageResponse = memberService.createMyPageResponse(pageable);
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


