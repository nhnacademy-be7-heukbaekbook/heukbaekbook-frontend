package com.nhnacademy.heukbaekfrontend.memberset.address.service.impl;

import com.nhnacademy.heukbaekfrontend.memberset.address.client.MemberAddressClient;
import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressDto;
import com.nhnacademy.heukbaekfrontend.memberset.address.service.MemberAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberAddressServiceImpl implements MemberAddressService {

    private final MemberAddressClient memberAddressClient;

    @Override
    public List<MemberAddressDto> getMemberAddressesList() {
        ResponseEntity<List<MemberAddressDto>> responseEntity = memberAddressClient.getAllMemberAddresses();
        return responseEntity.getBody().isEmpty() ? List.of() : responseEntity.getBody();
    }

    @Override
    public Long countMemberAddresses() {
        ResponseEntity<Long> responseEntity = memberAddressClient.countMemberAddresses();
        return Objects.isNull(responseEntity.getBody()) ? 0L : responseEntity.getBody();
    }


}
