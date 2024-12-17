package com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.service.impl;

import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.client.CouponPolicyClient;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.DiscountType;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.request.CouponPolicyRequest;
import com.nhnacademy.heukbaekfrontend.couponset.couponpolicy.dto.response.CouponPolicyResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponPolicyServiceImplTest {

    @InjectMocks
    private CouponPolicyServiceImpl couponPolicyService;

    @Mock
    private CouponPolicyClient couponPolicyClient;

    @Test
    void testGetCouponPolicies_Success() {
        // Given
        PageRequest pageable = PageRequest.of(0, 5);
        List<CouponPolicyResponse> mockPolicies = List.of(
                new CouponPolicyResponse(1L, DiscountType.PERCENTAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(100), BigDecimal.valueOf(500)),
                new CouponPolicyResponse(2L, DiscountType.FIXED, BigDecimal.valueOf(50), BigDecimal.valueOf(200), BigDecimal.valueOf(1000))
        );
        Page<CouponPolicyResponse> mockPage = new PageImpl<>(mockPolicies, pageable, mockPolicies.size());

        when(couponPolicyClient.getCouponPolicies(pageable.getPageNumber(), pageable.getPageSize(), "discountAmount,asc"))
                .thenReturn(ResponseEntity.ok(mockPage));

        // When
        Page<CouponPolicyResponse> result = couponPolicyService.getCouponPolicies(pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).discountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(result.getContent().get(1).discountAmount()).isEqualTo(BigDecimal.valueOf(50));

        verify(couponPolicyClient, times(1))
                .getCouponPolicies(pageable.getPageNumber(), pageable.getPageSize(), "discountAmount,asc");
    }

    @Test
    void testGetCouponPolicyList_Success() {
        // Given
        List<CouponPolicyResponse> mockPolicies = List.of(
                new CouponPolicyResponse(1L, DiscountType.PERCENTAGE, BigDecimal.valueOf(10), BigDecimal.valueOf(100), BigDecimal.valueOf(500)),
                new CouponPolicyResponse(2L, DiscountType.FIXED, BigDecimal.valueOf(50), BigDecimal.valueOf(200), BigDecimal.valueOf(1000))
        );

        when(couponPolicyClient.getCouponPolicyList()).thenReturn(ResponseEntity.ok(mockPolicies));

        // When
        List<CouponPolicyResponse> result = couponPolicyService.getCouponPolicyList();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).discountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(result.get(1).discountAmount()).isEqualTo(BigDecimal.valueOf(50));

        verify(couponPolicyClient, times(1)).getCouponPolicyList();
    }

    @Test
    void testCreateCouponPolicy_Success() {
        // Given
        CouponPolicyRequest request = new CouponPolicyRequest(
                DiscountType.PERCENTAGE,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(500)
        );

        CouponPolicyResponse response = new CouponPolicyResponse(
                1L,
                DiscountType.PERCENTAGE,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(500)
        );

        when(couponPolicyClient.createCouponPolicy(request)).thenReturn(ResponseEntity.ok(response));

        // When
        Optional<CouponPolicyResponse> result = couponPolicyService.createCouponPolicy(request);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1L);
        assertThat(result.get().discountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(result.get().discountAmount()).isEqualTo(BigDecimal.valueOf(10));
        verify(couponPolicyClient, times(1)).createCouponPolicy(request);
    }

    @Test
    void testUpdateCouponPolicy_Success() {
        // Given
        Long policyId = 1L;
        CouponPolicyRequest request = new CouponPolicyRequest(
                DiscountType.PERCENTAGE,
                BigDecimal.valueOf(15),
                BigDecimal.valueOf(200),
                BigDecimal.valueOf(1000)
        );

        CouponPolicyResponse response = new CouponPolicyResponse(
                policyId,
                DiscountType.PERCENTAGE,
                BigDecimal.valueOf(15),
                BigDecimal.valueOf(200),
                BigDecimal.valueOf(1000)
        );

        when(couponPolicyClient.updateCouponPolicy(policyId, request)).thenReturn(ResponseEntity.ok(response));

        // When
        Optional<CouponPolicyResponse> result = couponPolicyService.updateCouponPolicy(policyId, request);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(policyId);
        assertThat(result.get().discountType()).isEqualTo(DiscountType.PERCENTAGE);
        assertThat(result.get().discountAmount()).isEqualTo(BigDecimal.valueOf(15));
        verify(couponPolicyClient, times(1)).updateCouponPolicy(policyId, request);
    }

    @Test
    void testDeleteCouponPolicy_Success() {
        // Given
        Long policyId = 1L;

        when(couponPolicyClient.deleteCouponPolicy(policyId)).thenReturn(ResponseEntity.noContent().build());

        // When
        couponPolicyService.deleteCouponPolicy(policyId);

        // Then
        verify(couponPolicyClient, times(1)).deleteCouponPolicy(policyId);
    }

}
