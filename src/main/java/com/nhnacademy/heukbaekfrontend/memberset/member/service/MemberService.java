package com.nhnacademy.heukbaekfrontend.memberset.member.service;

import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberAddressDto;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberCreateRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberUpdateRequest;
import feign.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Optional<MemberResponse> signup(MemberCreateRequest memberCreateRequest);

    ResponseEntity<Boolean> existsLoginId(String loginId);

    ResponseEntity<Boolean> existsEmail(String email);

    ResponseEntity<MemberResponse> getMember();

    ResponseEntity<MemberResponse> updateMember(MemberUpdateRequest memberUpdateRequest);

    ResponseEntity<List<MemberAddressDto>> getAllMemberAddress()
}
