package com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto;

import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CouponResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;

import java.time.LocalDateTime;

public record MemberCouponResponse(
        CouponResponse couponResponse,
        MemberResponse memberResponse,
        Boolean isCouponUsed,
        LocalDateTime couponIssuedAt,
        LocalDateTime couponExpirationDate
) {}
