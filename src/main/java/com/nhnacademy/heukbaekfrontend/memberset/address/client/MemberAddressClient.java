package com.nhnacademy.heukbaekfrontend.memberset.address.client;

import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressRequest;
import com.nhnacademy.heukbaekfrontend.memberset.address.dto.MemberAddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "memberAddressClient", url = "http://localhost:8082/api/members/addresses")
public interface MemberAddressClient {

    @GetMapping
    ResponseEntity<List<MemberAddressResponse>> getAllMemberAddresses();

    @GetMapping("/count")
    ResponseEntity<Long> countMemberAddresses();

    @PostMapping
    ResponseEntity<MemberAddressResponse> createMemberAddress(@RequestBody MemberAddressRequest memberAddressRequest);

    @PutMapping("/{addressId}")
    ResponseEntity<MemberAddressResponse> updateMemberAddress(@PathVariable Long addressId, @RequestBody MemberAddressRequest memberAddressRequest);

    @DeleteMapping("/{addressId}")
    ResponseEntity<Void> deleteMemberAddress(@PathVariable Long addressId);
}
