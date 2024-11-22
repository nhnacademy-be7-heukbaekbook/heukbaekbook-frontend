package com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.service;

import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.request.CouponPolicyRequest;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.response.CouponPolicyResponse;
import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CouponPolicyService {
    Page<CouponPolicyResponse> getCouponPolicies(Pageable pageable);
    List<CouponPolicyResponse> getCouponPolicyList();
    Optional<CouponPolicyResponse> createCouponPolicy(CouponPolicyRequest couponPolicyRequest);
    Optional<CouponPolicyResponse> updateCouponPolicy(Long policyId, CouponPolicyRequest couponPolicyRequest);
    void deleteCouponPolicy(Long policyId);

}
