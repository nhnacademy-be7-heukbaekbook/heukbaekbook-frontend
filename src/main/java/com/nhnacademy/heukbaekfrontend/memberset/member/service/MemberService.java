package com.nhnacademy.heukbaekfrontend.memberset.member.service;

import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.*;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberAddressDto;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberCreateRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberUpdateRequest;
import com.nhnacademy.heukbaekfrontend.oauth.dto.OAuthMemberCreateRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Optional<MemberResponse> signup(MemberCreateRequest memberCreateRequest);

    Optional<MemberResponse> signupOAuth(OAuthMemberCreateRequest oAuthMemberCreateRequest);

    ResponseEntity<Boolean> existsLoginId(String loginId);

    ResponseEntity<Boolean> existsEmail(String email);

    ResponseEntity<MemberResponse> getMember();

    Optional<MemberResponse> updateMember(MemberUpdateRequest memberUpdateRequest);

    boolean deleteMember();

    Optional<GradeDto> getMembersGrade();

//    ResponseEntity<List<MemberAddressDto>> getAllMemberAddress();

    MyPageResponse createMyPageResponse(Pageable pageable);

    MyPageOrderDetailResponse getMyPageOrderDetail(String orderId);
}
