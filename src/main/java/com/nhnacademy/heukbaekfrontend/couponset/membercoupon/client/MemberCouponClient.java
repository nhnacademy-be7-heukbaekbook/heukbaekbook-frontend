package com.nhnacademy.heukbaekfrontend.couponset.membercoupon.client;

import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto.CouponHistoryResponse;
import com.nhnacademy.heukbaekfrontend.couponset.membercoupon.dto.MemberCouponResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "memberCouponClient", url = "http://localhost:8082/api")
public interface MemberCouponClient {

    @GetMapping("/members/coupons")
    Page<MemberCouponResponse> getAvailableCoupons();

    @GetMapping("/members/coupons/histories")
    Page<CouponHistoryResponse> getUsedCoupons();
}

