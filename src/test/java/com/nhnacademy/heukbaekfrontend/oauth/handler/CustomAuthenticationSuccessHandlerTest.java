package com.nhnacademy.heukbaekfrontend.oauth.handler;

import com.nhnacademy.heukbaekfrontend.common.dto.LoginResponse;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.client.LoginClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

import static org.mockito.Mockito.*;

class CustomAuthenticationSuccessHandlerTest {

    private final MemberService memberService = mock(MemberService.class);
    private final LoginClient loginClient = mock(LoginClient.class);
    private final CookieUtil cookieUtil = mock(CookieUtil.class);

    private final CustomAuthenticationSuccessHandler handler =
            new CustomAuthenticationSuccessHandler(memberService, loginClient, cookieUtil);

    @Test
    void onAuthenticationSuccess_ShouldRedirectToHome_WhenMemberExists() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);

        String idNo = "1234567890";
        LoginResponse loginResponse = new LoginResponse(
                1L,
                "ROLE_USER",
                "access-token",
                "refresh-token",
                3600000L,
                7200000L
        );

        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttributes()).thenReturn(Map.of("idNo", idNo));
        when(memberService.existsLoginId(idNo)).thenReturn(ResponseEntity.ok(true));
        when(loginClient.login(any(LoginRequest.class))).thenReturn(ResponseEntity.ok(loginResponse));

        // Act
        handler.onAuthenticationSuccess(request, response, authentication);

        // Assert
        verify(cookieUtil).addCookie(response, "accessToken", "access-token", 3600);
        verify(cookieUtil).addCookie(response, "refreshToken", "refresh-token", 7200);
        verify(response).sendRedirect("/");
    }

    @Test
    void onAuthenticationSuccess_ShouldRedirectToLoginWithError_WhenIdNoIsMissing() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);

        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttributes()).thenReturn(Map.of());

        // Act
        handler.onAuthenticationSuccess(request, response, authentication);

        // Assert
        verify(response).sendRedirect("/login?error=true&message=ID 번호가 제공되지 않았습니다.");
    }
}
