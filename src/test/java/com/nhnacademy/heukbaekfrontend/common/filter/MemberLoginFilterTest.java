package com.nhnacademy.heukbaekfrontend.common.filter;

import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.common.dto.LoginResponse;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.client.LoginClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemberLoginFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private LoginClient loginClient;

    @Mock
    private CookieUtil cookieUtil;

    @Mock
    private CartService cartService;

    private MemberLoginFilter memberLoginFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        memberLoginFilter = new MemberLoginFilter(authenticationManager, loginClient, cookieUtil, cartService);
    }

    @Test
    void testAttemptAuthentication_Success() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("loginId", "testUser");
        request.setParameter("password", "testPassword");

        LoginResponse loginResponse = new LoginResponse(
                1L,
                "ROLE_USER",
                "accessToken123",
                "refreshToken123",
                3600000L,
                7200000L
        );
        when(loginClient.login(any())).thenReturn(ResponseEntity.ok(loginResponse));

        // Act
        Authentication authentication = memberLoginFilter.attemptAuthentication(request, new MockHttpServletResponse());

        // Assert
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(1L);
        assertThat(authentication.getAuthorities()).hasSize(1);
        verify(loginClient).login(any(LoginRequest.class));
    }

    @Test
    void testAttemptAuthentication_Failure() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("loginId", "testUser");
        request.setParameter("password", "wrongPassword");

        when(loginClient.login(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        try {
            memberLoginFilter.attemptAuthentication(request, new MockHttpServletResponse());
        } catch (AuthenticationServiceException e) {
            assertThat(e.getMessage()).contains("로그인 요청 실패");
            assertThat(e.getCause()).isInstanceOf(BadCredentialsException.class);
            assertThat(e.getCause().getMessage()).isEqualTo("Invalid credentials");
        }

        verify(loginClient).login(any(LoginRequest.class));
    }


    @Test
    void testSuccessfulAuthentication() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        LoginResponse loginResponse = new LoginResponse(
                1L,
                "ROLE_USER",
                "accessToken123",
                "refreshToken123",
                3600L * 1000,
                7200L * 1000
        );

        Authentication authentication = mock(Authentication.class);
        when(authentication.getDetails()).thenReturn(loginResponse);

        // Act
        memberLoginFilter.successfulAuthentication(request, response, null, authentication);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isEqualTo(authentication);
        verify(cookieUtil).addCookie(response, "accessToken", "accessToken123", 3600);
        verify(cookieUtil).addCookie(response, "refreshToken", "refreshToken123", 7200);
        verify(cartService).synchronizeCartToDb(anyString());
        assertThat(response.getRedirectedUrl()).isEqualTo("/");
    }

    @Test
    void testUnsuccessfulAuthentication() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        memberLoginFilter.unsuccessfulAuthentication(request, response, new BadCredentialsException("Authentication failed"));

        // Assert
        assertThat(response.getStatus()).isEqualTo(401); // HttpStatus.UNAUTHORIZED
    }
}
