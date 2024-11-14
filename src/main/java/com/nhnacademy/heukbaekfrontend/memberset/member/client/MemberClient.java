package com.nhnacademy.heukbaekfrontend.memberset.member.client;

import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberCreateRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberUpdateRequest;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "signupClient", url = "http://localhost:8082/api/members")
public interface MemberClient {

    @PostMapping
    ResponseEntity<MemberResponse> signup(@RequestBody MemberCreateRequest memberCreateRequest);

    @GetMapping("/existsLoginId/{loginId}")
    ResponseEntity<Boolean> existsLoginId(@PathVariable String loginId);

    @GetMapping("/existsEmail/{email}")
    ResponseEntity<Boolean> existsEmail(@PathVariable String email);

    @GetMapping
    ResponseEntity<MemberResponse> getMemberInfo();

    @PutMapping
    ResponseEntity<MemberResponse> updateMember(@RequestBody MemberUpdateRequest memberUpdateRequest);

    @DeleteMapping
    ResponseEntity<MemberResponse> deleteMember();
}
