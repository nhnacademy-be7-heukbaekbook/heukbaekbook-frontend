package com.nhnacademy.heukbaekfrontend.memberset.address.client;

import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "memberAddressClient", url = "http://localhost:8082/api/members/addresses")
public interface MemberAddressClient {

    @GetMapping
    ResponseEntity<List<MemberAddressDto>> getAllMemberAddresses();

    @GetMapping("/count")
    ResponseEntity<Long> countMemberAddresses();
}
