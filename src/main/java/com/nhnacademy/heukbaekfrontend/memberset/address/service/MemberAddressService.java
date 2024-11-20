package com.nhnacademy.heukbaekfrontend.memberset.address.service;

import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressRequest;
import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface MemberAddressService {

    List<MemberAddressResponse> getMemberAddressesList();

    Optional<MemberAddressResponse> addMemberAddress(MemberAddressRequest memberAddressRequest);

    Optional<MemberAddressResponse> updateMemberAddress(Long addressId, MemberAddressRequest memberAddressRequest);

    void deleteMemberAddress(Long addressId);

    ResponseEntity<Long> countMemberAddresses();
}
