package com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto;

import java.time.LocalDateTime;

public record CouponIssueRequest(
        Long couponId,
        Long customerId,
        LocalDateTime couponExpirationDate
){
}
