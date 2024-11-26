package com.nhnacademy.heukbaekfrontend.memberset.member.service;

import com.nhnacademy.heukbaekfrontend.memberset.member.dto.*;
import feign.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Optional<MemberResponse> signup(MemberCreateRequest memberCreateRequest);

    ResponseEntity<Boolean> existsLoginId(String loginId);

    ResponseEntity<Boolean> existsEmail(String email);

    ResponseEntity<MemberResponse> getMember();

    Optional<MemberResponse> updateMember(MemberUpdateRequest memberUpdateRequest);

    boolean deleteMember();

    ResponseEntity<List<MemberAddressDto>> getAllMemberAddress();

    MyPageResponse createMyPageResponse();
}
