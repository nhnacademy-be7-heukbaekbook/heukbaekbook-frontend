package com.nhnacademy.heukbaekfrontend.memberset.member.client;

import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberCreateRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberUpdateRequest;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "signupClient", url = "http://localhost:8082")
public interface MemberClient {

    @PostMapping("/api/members")
    ResponseEntity<MemberResponse> signup(@RequestBody MemberCreateRequest memberCreateRequest);

    @PostMapping("/api/members/existsLoginId")
    ResponseEntity<Boolean> existsLoginId(@RequestBody String loginId);

    @PostMapping("/api/members/existsEmail")
    ResponseEntity<Boolean> existsEmail(@RequestBody String email);

    @GetMapping("/api/members")
    ResponseEntity<MemberResponse> getMemberInfo();

    @PutMapping("/api/members")
    ResponseEntity<MemberResponse> updateMember(@RequestBody MemberUpdateRequest memberUpdateRequest);

}
