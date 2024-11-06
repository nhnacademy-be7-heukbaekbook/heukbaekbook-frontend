package com.nhnacademy.heukbaekfrontend.member.client;

import com.nhnacademy.heukbaekfrontend.member.dto.MemberCreateRequest;
import com.nhnacademy.heukbaekfrontend.member.dto.MemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "signupClient", url = "http://localhost:8082")
public interface MemberClient {

    @PostMapping("/api/members")
    ResponseEntity<MemberResponse> signup(@RequestBody MemberCreateRequest memberCreateRequest);

    @PostMapping("/api/members/existsLoginId")
    ResponseEntity<Boolean> existsLoginId(@RequestBody String loginId);

    @PostMapping("/api/members/existsEmail")
    ResponseEntity<Boolean> existsEmail(@RequestBody String email);

}
