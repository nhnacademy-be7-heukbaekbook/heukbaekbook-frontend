package com.nhnacademy.heukbaekfrontend.common.filter;

import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.common.dto.LoginResponse;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BaseLoginFilterTest {

    private BaseLoginFilter baseLoginFilter;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CookieUtil cookieUtil;

    @Mock
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        baseLoginFilter = new BaseLoginFilter(authenticationManager, cookieUtil, "/test/login", cartService) {
            @Override
            protected ResponseEntity<LoginResponse> performLogin(LoginRequest loginRequest) {
                // Mock performLogin
                return ResponseEntity.ok(new LoginResponse(1L, "ROLE_USER", "access-token", "refresh-token", 3600L, 7200L));
            }

            @Override
            protected String getSuccessRedirectUrl() {
                return "/home";
            }
        };
    }

    @Test
    void testAttemptAuthentication() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("loginId", "testUser");
        request.setParameter("password", "testPassword");

        // Act
        Authentication authentication = baseLoginFilter.attemptAuthentication(request, new MockHttpServletResponse());

        // Assert
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(1L);
        assertThat(authentication.getAuthorities()).hasSize(1);
        assertThat(authentication.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    void testSuccessfulAuthentication() throws IOException, ServletException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        LoginResponse loginResponse = new LoginResponse(1L, "ROLE_USER", "access-token", "refresh-token", 3600L, 7200L);
        UsernamePasswordAuthenticationToken authResult = new UsernamePasswordAuthenticationToken(
                loginResponse.userId(),
                null,
                List.of(() -> "ROLE_USER")
        );
        authResult.setDetails(loginResponse);

        // Act
        baseLoginFilter.successfulAuthentication(request, response, filterChain, authResult);

        // Assert
        verify(cookieUtil, times(1)).addCookie(eq(response), eq("accessToken"), eq("access-token"), eq(3600L / 1000));
        verify(cookieUtil, times(1)).addCookie(eq(response), eq("refreshToken"), eq("refresh-token"), eq(7200L / 1000));
        verify(cartService, times(1)).synchronizeCartToDb(request.getSession().getId());
        assertThat(response.getRedirectedUrl()).isEqualTo("/home");
    }

    @Test
    void testUnsuccessfulAuthentication() throws IOException, ServletException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        baseLoginFilter.unsuccessfulAuthentication(request, response, new BadCredentialsException("Authentication failed"));

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_FOUND);
        assertThat(response.getRedirectedUrl()).isEqualTo("/login");
    }
}
