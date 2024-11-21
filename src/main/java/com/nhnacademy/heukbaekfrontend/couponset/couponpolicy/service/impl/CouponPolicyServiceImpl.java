package com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.service.impl;

import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.client.CouponPolicyClient;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.request.CouponPolicyRequest;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.response.CouponPolicyResponse;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponPolicyServiceImpl implements CouponPolicyService {

    private final CouponPolicyClient couponPolicyClient;

    @Override
    public Page<CouponPolicyResponse> getCouponPolicies(Pageable pageable) {
        ResponseEntity<Page<CouponPolicyResponse>> couponPolicies = couponPolicyClient.getCouponPolicies(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "discountAmount,asc");
        return couponPolicies.getBody();
    }

    @Override
    public List<CouponPolicyResponse> getCouponPolicyList() {
        return couponPolicyClient.getCouponPolicyList().getBody();
    }

    @Override
    public Optional<CouponPolicyResponse> createCouponPolicy(CouponPolicyRequest couponPolicyRequest) {
        ResponseEntity<CouponPolicyResponse> responseEntity = couponPolicyClient.createCouponPolicy(couponPolicyRequest);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Optional<CouponPolicyResponse> updateCouponPolicy(Long policyId, CouponPolicyRequest couponPolicyRequest) {
        ResponseEntity<CouponPolicyResponse> responseEntity = couponPolicyClient.updateCouponPolicy(policyId, couponPolicyRequest);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public void deleteCouponPolicy(Long policyId) {
        couponPolicyClient.deleteCouponPolicy(policyId);
    }


}
