package com.nhnacademy.heukbaekfrontend.memberset.address.service;

import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressDto;

import java.util.List;

public interface MemberAddressService {

    List<MemberAddressDto> getMemberAddressesList();

    Long countMemberAddresses();
}
