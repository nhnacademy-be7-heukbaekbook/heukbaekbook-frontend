package com.nhnacademy.heukbaekfrontend.memberset.address.service.impl;

import com.nhnacademy.heukbaekfrontend.memberset.address.client.MemberAddressClient;
import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressRequest;
import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressResponse;
import com.nhnacademy.heukbaekfrontend.memberset.address.service.MemberAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberAddressServiceImpl implements MemberAddressService {

    private final MemberAddressClient memberAddressClient;

    @Override
    public List<MemberAddressResponse> getMemberAddressesList() {
        ResponseEntity<List<MemberAddressResponse>> responseEntity = memberAddressClient.getAllMemberAddresses();
        return responseEntity.getBody().isEmpty() ? List.of() : responseEntity.getBody();
    }

    @Override
    public Optional<MemberAddressResponse> addMemberAddress(MemberAddressRequest memberAddressRequest) {
        ResponseEntity<MemberAddressResponse> responseEntity = memberAddressClient.createMemberAddress(memberAddressRequest);

        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Optional<MemberAddressResponse> updateMemberAddress(Long addressId, MemberAddressRequest memberAddressRequest) {
        ResponseEntity<MemberAddressResponse> responseEntity = memberAddressClient.updateMemberAddress(addressId, memberAddressRequest);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public void deleteMemberAddress(Long addressId) {
        memberAddressClient.deleteMemberAddress(addressId);
    }

    @Override
    public ResponseEntity<Long> countMemberAddresses() {
        return memberAddressClient.countMemberAddresses();
    }

}
