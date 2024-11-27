package com.nhnacademy.heukbaekfrontend.memberset.member.service.impl;

import com.nhnacademy.heukbaekfrontend.memberset.member.client.MemberClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.*;
import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberClient memberClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignup_Success() {
        // Given
        MemberCreateRequest request = new MemberCreateRequest(
                "user",
                "pass",
                Date.valueOf("2000-01-01"),
                "p",
                "010-1234-5678",
                "u@example.com",
                12345L,
                "seoul",
                "Address",
                "Home"
        );

        GradeDto gradeDto = new GradeDto(
                "Basic",
                BigDecimal.valueOf(0.10),
                BigDecimal.valueOf(1000)
        );

        MemberResponse mockResponse = new MemberResponse(
                "p",
                "010-1234-5678",
                "u@example.com",
                "user",
                Date.valueOf("2000-01-01"),
                LocalDateTime.now(),
                null,
                MemberStatus.ACTIVE,
                gradeDto
        );

        when(memberClient.signup(any(MemberCreateRequest.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        // When
        Optional<MemberResponse> result = memberService.signup(request);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("p");
        verify(memberClient, times(1)).signup(any(MemberCreateRequest.class));
    }

    @Test
    void testSignup_Failure() {
        // Given
        MemberCreateRequest request = new MemberCreateRequest(
                "user123",
                "password@1",
                Date.valueOf("2000-01-01"),
                "홍길동",
                "010-1234-5678",
                "user123@example.com",
                12345L,
                "Seoul Road",
                "Detail Address",
                "Home"
        );

        when(memberClient.signup(any(MemberCreateRequest.class)))
                .thenThrow(FeignException.class);

        // When
        Optional<MemberResponse> result = memberService.signup(request);

        // Then
        assertThat(result).isEmpty();
        verify(memberClient, times(1)).signup(any(MemberCreateRequest.class));
    }
    @Test
    void testExistsLoginId_Success() {
        // Given
        when(memberClient.existsLoginId(anyString()))
                .thenReturn(new ResponseEntity<>(true, HttpStatus.OK));

        // When
        ResponseEntity<Boolean> result = memberService.existsLoginId("user123");

        // Then
        assertThat(result.getBody()).isTrue();
        verify(memberClient, times(1)).existsLoginId(anyString());
    }

    @Test
    void testExistsLoginId_Failure() {
        // Given
        when(memberClient.existsLoginId(anyString()))
                .thenThrow(FeignException.class);

        // When & Then
        assertThrows(RuntimeException.class, () -> memberService.existsLoginId("user123"));
        verify(memberClient, times(1)).existsLoginId(anyString());
    }

    @Test
    void testExistsEmail_Success() {
        // Given
        when(memberClient.existsEmail(anyString()))
                .thenReturn(new ResponseEntity<>(true, HttpStatus.OK));

        // When
        ResponseEntity<Boolean> result = memberService.existsEmail("user123@example.com");

        // Then
        assertThat(result.getBody()).isTrue();
        verify(memberClient, times(1)).existsEmail(anyString());
    }

    @Test
    void testExistsEmail_Failure() {
        // Given
        when(memberClient.existsEmail(anyString()))
                .thenThrow(FeignException.class);

        // When & Then
        assertThrows(RuntimeException.class, () -> memberService.existsEmail("user123@example.com"));
        verify(memberClient, times(1)).existsEmail(anyString());
    }

    @Test
    void testGetMember_Success() {
        // Given
        MemberResponse mockResponse = mock(MemberResponse.class);
        when(memberClient.getMemberInfo())
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // When
        ResponseEntity<MemberResponse> result = memberService.getMember();

        // Then
        assertThat(result.getBody()).isNotNull();
        verify(memberClient, times(1)).getMemberInfo();
    }

    @Test
    void testGetMember_Failure() {
        // Given
        when(memberClient.getMemberInfo())
                .thenThrow(FeignException.class);

        // When & Then
        assertThrows(RuntimeException.class, () -> memberService.getMember());
        verify(memberClient, times(1)).getMemberInfo();
    }

    @Test
    void testUpdateMember_Success() {
        // Given
        MemberUpdateRequest request = mock(MemberUpdateRequest.class);
        MemberResponse mockResponse = mock(MemberResponse.class);

        when(memberClient.updateMember(any(MemberUpdateRequest.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // When
        Optional<MemberResponse> result = memberService.updateMember(request);

        // Then
        assertThat(result).isPresent();
        verify(memberClient, times(1)).updateMember(any(MemberUpdateRequest.class));
    }

    @Test
    void testDeleteMember_Success() {
        // Given
        when(memberClient.deleteMember())
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // When
        boolean result = memberService.deleteMember();

        // Then
        assertThat(result).isTrue();
        verify(memberClient, times(1)).deleteMember();
    }

    @Test
    void testDeleteMember_Failure() {
        // Given
        when(memberClient.deleteMember())
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        // When
        boolean result = memberService.deleteMember();

        // Then
        assertThat(result).isFalse();
        verify(memberClient, times(1)).deleteMember();
    }

    @Test
    void testGetAllMemberAddress_NotImplemented() {
        // When
        ResponseEntity<List<MemberAddressDto>> result = memberService.getAllMemberAddress();

        // Then
        assertThat(result).isNull(); // 해당 메서드가 현재 구현되지 않음
    }
}
