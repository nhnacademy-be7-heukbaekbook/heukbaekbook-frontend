package com.nhnacademy.heukbaekfrontend.memberset.member.dto;

import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressResponse;

import java.math.BigDecimal;
import java.util.List;

public record MemberDetailResponse(
        Long id,
        String customerName,
        String customerPhoneNumber,
        String customerEmail,
        BigDecimal point,
        List<MemberAddressResponse> memberAddresses) {
}
