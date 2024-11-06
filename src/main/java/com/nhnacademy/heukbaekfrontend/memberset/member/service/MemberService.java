package com.nhnacademy.heukbaekfrontend.memberset.member.service;

import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberCreateRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface MemberService {
    Optional<MemberResponse> signup(MemberCreateRequest memberCreateRequest);

    ResponseEntity<Boolean> existsLoginId(String loginId);

    ResponseEntity<Boolean> existsEmail(String email);
}
