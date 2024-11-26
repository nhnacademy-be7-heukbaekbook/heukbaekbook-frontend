package com.nhnacademy.heukbaekfrontend.memberset.member.dto;

import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressResponse;

import java.util.List;

public record MemberDetailResponse(Long id,
                                   String customerName,
                                   String customerPhoneNumber,
                                   String customerEmail,
                                   List<MemberAddressResponse> memberAddresses) {
}
