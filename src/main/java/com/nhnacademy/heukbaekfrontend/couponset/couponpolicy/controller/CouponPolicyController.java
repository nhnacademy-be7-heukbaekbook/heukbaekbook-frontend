package com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.controller;

import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.DiscountType;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.request.CouponPolicyRequest;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.response.CouponPolicyResponse;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.service.CouponPolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons/policy")
public class CouponPolicyController {
    private final String REDIRECT_PATH = "redirect:/admin/coupons/policy";

    private final CouponPolicyService couponPolicyService;

    @ModelAttribute
    public void before(Model model, Pageable pageable) {
        Page<CouponPolicyResponse> couponPolicies = couponPolicyService.getCouponPolicies(pageable);

        model.addAttribute("couponPolicies", couponPolicies);
        model.addAttribute("policyTypes", DiscountType.values());
    }

    @GetMapping
    public String getCouponPolicyPage() {
        return "coupon/admin/coupon-policy";
    }

    @PostMapping
    public String addCouponPolicy(@Valid @ModelAttribute CouponPolicyRequest couponPolicyRequest,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("couponPolicyRequest", couponPolicyRequest);
            return REDIRECT_PATH;
        }
        couponPolicyService.createCouponPolicy(couponPolicyRequest);
        return REDIRECT_PATH;
    }

    @PutMapping("/{policyId}")
    public String updateCouponPolicy(@PathVariable Long policyId, @ModelAttribute CouponPolicyRequest couponPolicyRequest) {
        couponPolicyService.updateCouponPolicy(policyId, couponPolicyRequest);
        return REDIRECT_PATH;
    }

    @DeleteMapping("/{policyId}")
    public String deleteCouponPolicy(@PathVariable Long policyId) {
        couponPolicyService.deleteCouponPolicy(policyId);
        return REDIRECT_PATH;
    }
}
