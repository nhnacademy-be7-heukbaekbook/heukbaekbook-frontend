package com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto;

import java.time.LocalDateTime;

public record MemberCouponResponse(
        Long memberCouponId,
        Long couponId,
        String couponName,
        String couponDescription,
        Boolean isCouponUsed,
        LocalDateTime couponIssuedAt,
        LocalDateTime couponExpirationDate
) {}
