package com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.client;

import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.request.CouponPolicyRequest;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.response.CouponPolicyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "couponPolicyClient", url = "http://localhost:8082/api/admin/coupons/policy")
public interface CouponPolicyClient {

    @GetMapping
    ResponseEntity<Page<CouponPolicyResponse>> getCouponPolicies(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "sort", required = false) String sort
    );

    @PostMapping
    ResponseEntity<CouponPolicyResponse> createCouponPolicy(@RequestBody CouponPolicyRequest couponPolicyRequest);

    @PutMapping("/{policyId}")
    ResponseEntity<CouponPolicyResponse> updateCouponPolicy(@PathVariable Long policyId, @RequestBody CouponPolicyRequest couponPolicyRequest);

    @DeleteMapping("/{policyId}")
    ResponseEntity<Void> deleteCouponPolicy(@PathVariable Long policyId);

}

