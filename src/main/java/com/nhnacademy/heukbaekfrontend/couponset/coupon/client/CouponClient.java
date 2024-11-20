package com.nhnacademy.heukbaekfrontend.couponset.coupon.client;

import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CouponResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "couponClient", url = "http://localhost:8082/api/admin/coupons")
public interface CouponClient {
    @GetMapping
    ResponseEntity<CouponResponse> getAllCoupons();

}
