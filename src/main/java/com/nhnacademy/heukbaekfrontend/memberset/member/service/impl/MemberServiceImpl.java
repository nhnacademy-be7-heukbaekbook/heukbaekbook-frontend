package com.nhnacademy.heukbaekfrontend.memberset.member.service.impl;

import com.nhnacademy.heukbaekfrontend.memberset.member.client.MemberClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.*;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {


    private final MemberClient memberClient;

    @Override
    public Optional<MemberResponse> signup(MemberCreateRequest memberCreateRequest) {
        try {
            ResponseEntity<MemberResponse> responseEntity = memberClient.signup(memberCreateRequest);
            return Optional.ofNullable(responseEntity.getBody());
        } catch (FeignException fe) {
            return Optional.empty();
        }
    }

    @Override
    public ResponseEntity<Boolean> existsLoginId(String loginId) {
        try {
            return memberClient.existsLoginId(loginId);
        } catch (FeignException fe) {
            throw new RuntimeException();
        }
    }

    @Override
    public ResponseEntity<Boolean> existsEmail(String email) {
        try {
            return memberClient.existsEmail(email);
        } catch (FeignException fe) {
            throw new RuntimeException();
        }
    }

    @Override
    public ResponseEntity<MemberResponse> getMember() {
        try {
            return memberClient.getMemberInfo();
        } catch (FeignException fe) {
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<MemberResponse> updateMember(MemberUpdateRequest memberUpdateRequest) {
        ResponseEntity<MemberResponse> responseEntity = memberClient.updateMember(memberUpdateRequest);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public boolean deleteMember() {
        ResponseEntity<MemberResponse> responseEntity = memberClient.deleteMember();
        return responseEntity.getStatusCode().is2xxSuccessful();
    }

    @Override
    public ResponseEntity<List<MemberAddressDto>> getAllMemberAddress() {
        return null;
    }

    @Override
    public MyPageResponse createMyPageResponse() {
        return memberClient.getMyPageResponse();
    }

    @Override
    public MyPageOrderDetailResponse getMyPageOrderDetail(String orderId) {
        return memberClient.getMyPageOrderDetail(orderId);
    }
}
