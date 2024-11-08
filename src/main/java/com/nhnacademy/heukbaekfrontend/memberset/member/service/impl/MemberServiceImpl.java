package com.nhnacademy.heukbaekfrontend.memberset.member.service.impl;

import com.nhnacademy.heukbaekfrontend.memberset.member.client.MemberClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberCreateRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberUpdateRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    public ResponseEntity<MemberResponse> updateMember(MemberUpdateRequest memberUpdateRequest) {
        try {
            return memberClient.updateMember(memberUpdateRequest);
        }catch (FeignException fe) {
            throw new RuntimeException();
        }
    }
}
