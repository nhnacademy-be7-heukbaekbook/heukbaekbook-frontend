package com.nhnacademy.heukbaekfrontend.couponset.coupon.client;

import com.nhnacademy.heukbaekfrontend.couponset.coupon.dto.response.CouponResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "couponClient", url = "http://localhost:8082/api/coupons")
public interface DownloadableCouponClient {
    @GetMapping("/book/{bookId}")
    ResponseEntity<List<CouponResponse>> getDownloadableCoupons(@PathVariable Long bookId);
}
