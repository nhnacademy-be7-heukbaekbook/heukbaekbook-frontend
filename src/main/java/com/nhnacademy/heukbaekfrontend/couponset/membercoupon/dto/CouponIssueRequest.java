package com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto;

public record CouponIssueRequest(
        Long couponId,
        Long customerId,
        int availableDuration
){
}
