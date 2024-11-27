package com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.controller;

import com.nhnacademy.heukbaekfrontend.couponset.coupon.controller.CouponController;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.DiscountType;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.request.CouponPolicyRequest;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.response.CouponPolicyResponse;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.service.CouponPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponPolicyController.class)
class CouponPolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponPolicyService couponPolicyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new CouponPolicyController(couponPolicyService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testGetCouponPolicyPage() throws Exception {
        // Mock CouponPolicyResponse 데이터 생성
        Page<CouponPolicyResponse> mockPolicies = new PageImpl<>(List.of(
                new CouponPolicyResponse(
                        1L, DiscountType.PERCENTAGE, new BigDecimal("10.00"),
                        new BigDecimal("50.00"), new BigDecimal("500.00")
                ),
                new CouponPolicyResponse(
                        2L, DiscountType.FIXED, new BigDecimal("1000.00"),
                        new BigDecimal("10000.00"), new BigDecimal("100000.00")
                )
        ));

        // Mock 서비스 동작 설정
        when(couponPolicyService.getCouponPolicies(PageRequest.of(0, 10))).thenReturn(mockPolicies);

        // 테스트 수행
        mockMvc.perform(get("/admin/coupons/policy")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("coupon/admin/coupon-policy"))
                .andExpect(model().attributeExists("couponPolicies"))
                .andExpect(model().attributeExists("policyTypes"))
                .andExpect(model().attribute("couponPolicies", mockPolicies))
                .andExpect(model().attribute("policyTypes", DiscountType.values()));
    }

    @Test
    void testAddCouponPolicyWithValidData() throws Exception {
        // 유효한 CouponPolicyRequest 데이터 생성
        CouponPolicyRequest validRequest = new CouponPolicyRequest(
                DiscountType.PERCENTAGE,
                new BigDecimal("10.00"),
                new BigDecimal("50.00"),
                new BigDecimal("500.00")
        );

        CouponPolicyResponse mockResponse = new CouponPolicyResponse(
                1L,
                DiscountType.PERCENTAGE,
                new BigDecimal("10.00"),
                new BigDecimal("50.00"),
                new BigDecimal("500.00")
        );
        // Mock 서비스 동작 설정
        when(couponPolicyService.createCouponPolicy(any(CouponPolicyRequest.class)))
                .thenReturn(Optional.of(mockResponse));

        // 테스트 수행
        mockMvc.perform(post("/admin/coupons/policy")
                        .flashAttr("couponPolicyRequest", validRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/coupons/policy"));
    }

    @Test
    void testUpdateCouponPolicyWithValidData() throws Exception {
        // 유효한 CouponPolicyRequest 데이터 생성
        CouponPolicyRequest validRequest = new CouponPolicyRequest(
                DiscountType.PERCENTAGE,
                new BigDecimal("10.00"),
                new BigDecimal("50.00"),
                new BigDecimal("500.00")
        );

        // Mock CouponPolicyResponse 데이터 생성
        CouponPolicyResponse mockResponse = new CouponPolicyResponse(
                1L,
                DiscountType.PERCENTAGE,
                new BigDecimal("10.00"),
                new BigDecimal("50.00"),
                new BigDecimal("500.00")
        );

        // Mock 서비스 동작 설정
        when(couponPolicyService.updateCouponPolicy(any(Long.class), any(CouponPolicyRequest.class)))
                .thenReturn(Optional.of(mockResponse));

        // 테스트 수행
        mockMvc.perform(put("/admin/coupons/policy/1")
                        .flashAttr("couponPolicyRequest", validRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/coupons/policy"));
    }

    @Test
    void testDeleteCouponPolicy() throws Exception {
        // Mock 서비스 동작 설정
        doNothing().when(couponPolicyService).deleteCouponPolicy(any(Long.class));

        // 테스트 수행
        mockMvc.perform(delete("/admin/coupons/policy/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/coupons/policy"));
    }

}
