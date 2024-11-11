package com.nhnacademy.heukbaekfrontend.memberset.member.client;

import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberCreateRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberUpdateRequest;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "signupClient", url = "http://localhost:8082")
public interface MemberClient {

    @PostMapping("/api/members")
    ResponseEntity<MemberResponse> signup(@RequestBody MemberCreateRequest memberCreateRequest);

    @GetMapping("/api/members/existsLoginId/{loginId}")
    ResponseEntity<Boolean> existsLoginId(@PathVariable String loginId);

    @GetMapping("/api/members/existsEmail/{email}")
    ResponseEntity<Boolean> existsEmail(@PathVariable String email);

    @GetMapping("/api/members")
    ResponseEntity<MemberResponse> getMemberInfo();

    @PutMapping("/api/members")
    ResponseEntity<MemberResponse> updateMember(@RequestBody MemberUpdateRequest memberUpdateRequest);

    @DeleteMapping("/api/members")
    ResponseEntity<MemberResponse> deleteMember();
}
