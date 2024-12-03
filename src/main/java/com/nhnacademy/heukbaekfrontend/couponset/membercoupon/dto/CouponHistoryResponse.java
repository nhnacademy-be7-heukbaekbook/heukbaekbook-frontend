package com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto;

import java.time.LocalDateTime;

public record CouponHistoryResponse(
        Long couponHistoryId,
        Long memberCouponId,
        Long memberId,
        Long couponId,
        LocalDateTime usedAt,
        Long bookId,
        Long orderId
) {}

