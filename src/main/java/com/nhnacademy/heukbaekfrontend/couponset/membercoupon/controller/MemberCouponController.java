package com.nhnacademy.heukbaekfrontend.couponset.membercoupon.controller;

import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.nhnacademy.heukbaekfrontend.util.Utils.getCustomerId;

@Controller
@RequestMapping("/members/coupons")
@RequiredArgsConstructor
public class MemberCouponController {

    private final CouponIssueService redisCouponService;

    @PostMapping("/{couponId}")
    public ResponseEntity<String> issueCoupon(@PathVariable("couponId") Long couponId) {

        Long customerId = Long.parseLong(getCustomerId());

        redisCouponService.issueCoupon(customerId, couponId);

        return null;
    }
}
