package com.nhnacademy.heukbaekfrontend.oauth.service;

import com.nhnacademy.heukbaekfrontend.common.dto.LoginResponse;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.client.LoginClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import com.nhnacademy.heukbaekfrontend.oauth.dto.AdditionalInfoCreate;
import com.nhnacademy.heukbaekfrontend.oauth.dto.OAuthMemberCreateRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OAuthAdditionalInfoServiceTest {

    private CookieUtil cookieUtil;
    private MemberService memberService;
    private LoginClient loginClient;
    private OAuthAdditionalInfoService service;

    @BeforeEach
    void setUp() {
        cookieUtil = mock(CookieUtil.class);
        memberService = mock(MemberService.class);
        loginClient = mock(LoginClient.class);
        service = new OAuthAdditionalInfoService(cookieUtil, memberService, loginClient);

        SecurityContextHolder.clearContext();
    }

    @Test
    void createAdditionalInfoForm_ShouldReturnAdditionalInfoCreate() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(cookieUtil.getCookieValue(request, "idNo")).thenReturn(encodeValue("testId"));
        when(cookieUtil.getCookieValue(request, "name")).thenReturn(encodeValue("testName"));
        when(cookieUtil.getCookieValue(request, "phoneNumber")).thenReturn(encodeValue("01012345678"));
        when(cookieUtil.getCookieValue(request, "email")).thenReturn(encodeValue("test@example.com"));

        // Act
        AdditionalInfoCreate result = service.createAdditionalInfoForm(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.idNo()).isEqualTo("testId");
        assertThat(result.name()).isEqualTo("testName");
        assertThat(result.phoneNumber()).isEqualTo("01012345678");
        assertThat(result.email()).isEqualTo("test@example.com");
        assertThat(result.birth()).isNull();
    }

    @Test
    void signupOAuth_ShouldReturnMemberResponse() {
        // Arrange
        AdditionalInfoCreate additionalInfoCreate = new AdditionalInfoCreate(
                "testIdNo1234", // 길이가 10 이상인 idNo로 수정
                "testName",
                "01012345678",
                "test@example.com",
                Date.valueOf("1990-01-01")
        );
        MemberResponse mockResponse = mock(MemberResponse.class);
        when(memberService.signupOAuth(any(OAuthMemberCreateRequest.class))).thenReturn(Optional.of(mockResponse));

        // Act
        Optional<MemberResponse> result = service.signupOAuth(additionalInfoCreate);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(mockResponse);
    }

    @Test
    void signupOAuth_ShouldHandleShortIdNo() {
        // Arrange
        AdditionalInfoCreate additionalInfoCreate = new AdditionalInfoCreate(
                "shortId123", // 최소 10자를 만족하는 idNo로 수정
                "testName",
                "01012345678",
                "test@example.com",
                Date.valueOf("1990-01-01")
        );
        MemberResponse mockResponse = mock(MemberResponse.class);
        when(memberService.signupOAuth(any(OAuthMemberCreateRequest.class))).thenReturn(Optional.of(mockResponse));

        // Act
        Optional<MemberResponse> result = service.signupOAuth(additionalInfoCreate);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(mockResponse);
    }

    @Test
    void processLogin_ShouldSetCookiesAndAuthentication() {
        // Arrange
        HttpServletResponse response = mock(HttpServletResponse.class);
        LoginResponse loginResponse = new LoginResponse(
                1L,
                "ROLE_USER",
                "accessToken123",
                "refreshToken123",
                3600000L,
                7200000L
        );
        when(loginClient.login(any(LoginRequest.class))).thenReturn(ResponseEntity.ok(loginResponse));

        // Act
        service.processLogin("testId", "testPassword", response);

        // Assert
        verify(cookieUtil, times(1)).addCookie(response, "accessToken", "accessToken123", 3600);
        verify(cookieUtil, times(1)).addCookie(response, "refreshToken", "refreshToken123", 7200);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo(1L);
    }

    private String encodeValue(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
