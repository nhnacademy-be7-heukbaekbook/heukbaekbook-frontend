package com.nhnacademy.heukbaekfrontend.memberset.member.service.impl;

import com.nhnacademy.heukbaekfrontend.memberset.grade.dto.GradeDto;
import com.nhnacademy.heukbaekfrontend.memberset.member.client.MemberClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.*;
import com.nhnacademy.heukbaekfrontend.oauth.dto.OAuthMemberCreateRequest;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Map;
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
                "user123",
                "Pass@1234",
                Date.valueOf("2000-01-01"),
                "홍길동",
                "010-1234-5678",
                "user@example.com",
                12345L,
                "서울특별시 강남구",
                "123호",
                "Home"
        );

        MemberResponse mockResponse = new MemberResponse(
                "홍길동",
                "010-1234-5678",
                "user@example.com",
                "user123",
                Date.valueOf("2000-01-01"),
                LocalDateTime.now(),
                null,
                MemberStatus.ACTIVE,
                new GradeDto("Basic", BigDecimal.valueOf(0.10), BigDecimal.valueOf(1000))
        );

        when(memberClient.signup(any(MemberCreateRequest.class))).thenReturn(ResponseEntity.ok(mockResponse));

        // When
        Optional<MemberResponse> result = memberService.signup(request);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("홍길동");
        verify(memberClient, times(1)).signup(any(MemberCreateRequest.class));
    }

    @Test
    void testSignup_Failure() {
        // Given
        MemberCreateRequest request = new MemberCreateRequest(
                "user123",
                "Pass@1234",
                Date.valueOf("2000-01-01"),
                "홍길동",
                "010-1234-5678",
                "user@example.com",
                12345L,
                "서울특별시 강남구",
                "123호",
                "Home"
        );

        when(memberClient.signup(any(MemberCreateRequest.class))).thenThrow(FeignException.class);

        // When
        Optional<MemberResponse> result = memberService.signup(request);

        // Then
        assertThat(result).isEmpty();
        verify(memberClient, times(1)).signup(any(MemberCreateRequest.class));
    }

    @Test
    void testExistsLoginId_Success() {
        // Given
        when(memberClient.existsLoginId(anyString())).thenReturn(new ResponseEntity<>(true, HttpStatus.OK));

        // When
        ResponseEntity<Boolean> result = memberService.existsLoginId("user123");

        // Then
        assertThat(result.getBody()).isTrue();
        verify(memberClient, times(1)).existsLoginId(anyString());
    }

    @Test
    void testExistsLoginId_Failure() {
        // Given
        when(memberClient.existsLoginId(anyString())).thenThrow(FeignException.class);

        // When & Then
        assertThrows(RuntimeException.class, () -> memberService.existsLoginId("user123"));
        verify(memberClient, times(1)).existsLoginId(anyString());
    }

    @Test
    void testExistsEmail_Success() {
        // Given
        when(memberClient.existsEmail(anyString())).thenReturn(new ResponseEntity<>(true, HttpStatus.OK));

        // When
        ResponseEntity<Boolean> result = memberService.existsEmail("user@example.com");

        // Then
        assertThat(result.getBody()).isTrue();
        verify(memberClient, times(1)).existsEmail(anyString());
    }

    @Test
    void testExistsEmail_Failure() {
        // Given
        when(memberClient.existsEmail(anyString())).thenThrow(FeignException.class);

        // When & Then
        assertThrows(RuntimeException.class, () -> memberService.existsEmail("user@example.com"));
        verify(memberClient, times(1)).existsEmail(anyString());
    }

    @Test
    void testDeleteMember_Success() {
        // Given
        when(memberClient.deleteMember()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // When
        boolean result = memberService.deleteMember();

        // Then
        assertThat(result).isTrue();
        verify(memberClient, times(1)).deleteMember();
    }

    @Test
    void testDeleteMember_Failure() {
        // Given
        when(memberClient.deleteMember()).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        // When
        boolean result = memberService.deleteMember();

        // Then
        assertThat(result).isFalse();
        verify(memberClient, times(1)).deleteMember();
    }

    @Test
    void testUpdateMember_Success() {
        // Given
        MemberUpdateRequest request = mock(MemberUpdateRequest.class);
        MemberResponse mockResponse = mock(MemberResponse.class);

        when(memberClient.updateMember(any(MemberUpdateRequest.class))).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // When
        Optional<MemberResponse> result = memberService.updateMember(request);

        // Then
        assertThat(result).isPresent();
        verify(memberClient, times(1)).updateMember(any(MemberUpdateRequest.class));
    }

    @Test
    void testUpdateMember_NullResponse() {
        // Given
        MemberUpdateRequest request = mock(MemberUpdateRequest.class);

        when(memberClient.updateMember(any(MemberUpdateRequest.class))).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        // When
        Optional<MemberResponse> result = memberService.updateMember(request);

        // Then
        assertThat(result).isEmpty();
        verify(memberClient, times(1)).updateMember(any(MemberUpdateRequest.class));
    }
    @Test
    void testGetMember_Success() {
        // Given
        MemberResponse mockResponse = new MemberResponse(
                "홍길동",
                "010-1234-5678",
                "user@example.com",
                "user123",
                Date.valueOf("2000-01-01"),
                LocalDateTime.now(),
                null,
                MemberStatus.ACTIVE,
                new GradeDto("VIP", BigDecimal.valueOf(0.15), BigDecimal.valueOf(2000))
        );

        when(memberClient.getMemberInfo()).thenReturn(ResponseEntity.ok(mockResponse));

        // When
        ResponseEntity<MemberResponse> result = memberService.getMember();

        // Then
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().name()).isEqualTo("홍길동");
        verify(memberClient, times(1)).getMemberInfo();
    }

    @Test
    void testGetMember_Failure() {
        // Given
        when(memberClient.getMemberInfo()).thenThrow(FeignException.class);

        // When & Then
        assertThrows(RuntimeException.class, () -> memberService.getMember());
        verify(memberClient, times(1)).getMemberInfo();
    }

    @Test
    void testCreateMyPageResponse_Success() {
        // Given
        MyPageResponse mockResponse = mock(MyPageResponse.class);
        PageRequest pageRequest = PageRequest.of(1, 10);
        when(memberClient.getMyPageResponse(pageRequest)).thenReturn(mockResponse);

        // When
        MyPageResponse result = memberService.createMyPageResponse(pageRequest);

        // Then
        assertThat(result).isNotNull();
        verify(memberClient, times(1)).getMyPageResponse(pageRequest);
    }

    @Test
    void testGetMyPageOrderDetail_Success() {
        // Given
        String orderId = "order123";
        MyPageOrderDetailResponse mockResponse = mock(MyPageOrderDetailResponse.class);
        when(memberClient.getMyPageOrderDetail(orderId)).thenReturn(mockResponse);

        // When
        MyPageOrderDetailResponse result = memberService.getMyPageOrderDetail(orderId);

        // Then
        assertThat(result).isNotNull();
        verify(memberClient, times(1)).getMyPageOrderDetail(orderId);
    }

    @Test
    void testGetMyPageOrderDetail_Failure() {
        // Given
        String orderId = "order123";
        when(memberClient.getMyPageOrderDetail(orderId)).thenThrow(FeignException.class);

        // When & Then
        assertThrows(FeignException.class, () -> memberService.getMyPageOrderDetail(orderId));
        verify(memberClient, times(1)).getMyPageOrderDetail(orderId);
    }

    @Test
    void testGetMembersGrade_Success() {
        // Given
        GradeDto mockGrade = new GradeDto("Gold", BigDecimal.valueOf(0.10), BigDecimal.valueOf(5000));
        when(memberClient.getMembersGrade()).thenReturn(ResponseEntity.ok(mockGrade));

        // When
        Optional<GradeDto> result = memberService.getMembersGrade();

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().gradeName()).isEqualTo("Gold");
        verify(memberClient, times(1)).getMembersGrade();
    }

    @Test
    void testGetMembersGrade_Failure() {
        // Given
        Request request = Request.create(Request.HttpMethod.GET, "/grade", Map.of(), null, new RequestTemplate());
        when(memberClient.getMembersGrade()).thenThrow(new FeignException.BadRequest("Bad Request", request, null, null));

        // When & Then
        assertThrows(FeignException.BadRequest.class, () -> memberService.getMembersGrade());

        verify(memberClient, times(1)).getMembersGrade();
    }

    @Test
    void testSignupOAuth_Success() {
        // Given
        OAuthMemberCreateRequest request = mock(OAuthMemberCreateRequest.class);
        MemberResponse mockResponse = mock(MemberResponse.class);

        when(memberClient.signupOAuth(any(OAuthMemberCreateRequest.class))).thenReturn(ResponseEntity.ok(mockResponse));

        // When
        Optional<MemberResponse> result = memberService.signupOAuth(request);

        // Then
        assertThat(result).isPresent();
        verify(memberClient, times(1)).signupOAuth(any(OAuthMemberCreateRequest.class));
    }

    @Test
    void testSignupOAuth_Failure() {
        // Given
        OAuthMemberCreateRequest request = mock(OAuthMemberCreateRequest.class);
        when(memberClient.signupOAuth(any(OAuthMemberCreateRequest.class))).thenThrow(FeignException.class);

        // When
        Optional<MemberResponse> result = memberService.signupOAuth(request);

        // Then
        assertThat(result).isEmpty();
        verify(memberClient, times(1)).signupOAuth(any(OAuthMemberCreateRequest.class));
    }


}
