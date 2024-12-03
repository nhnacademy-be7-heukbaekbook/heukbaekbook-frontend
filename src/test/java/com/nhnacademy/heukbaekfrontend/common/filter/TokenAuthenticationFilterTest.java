package com.nhnacademy.heukbaekfrontend.common.filter;

import com.nhnacademy.heukbaekfrontend.common.client.AuthClient;
import com.nhnacademy.heukbaekfrontend.common.dto.TokenRequest;
import com.nhnacademy.heukbaekfrontend.common.dto.TokenResponse;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.ACCESS_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TokenAuthenticationFilterTest {

    @Mock
    private AuthClient authClient;

    @Mock
    private CookieUtil cookieUtil;

    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tokenAuthenticationFilter = new TokenAuthenticationFilter(authClient, cookieUtil);
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        String token = "valid-token";
        TokenResponse tokenResponse = new TokenResponse(1L, "ROLE_USER");

        when(cookieUtil.getCookieValue(request, ACCESS_TOKEN)).thenReturn(token);
        when(authClient.validateMember(new TokenRequest(token))).thenReturn(ResponseEntity.ok(tokenResponse));

        // Act
        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo(1L);
        assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_USER");

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        String token = "invalid-token";
        when(cookieUtil.getCookieValue(request, ACCESS_TOKEN)).thenReturn(token);

        // Member token validation throws Unauthorized exception
        when(authClient.validateMember(new TokenRequest(token))).thenThrow(FeignException.Unauthorized.class);

        // Admin token validation returns null (no body)
        when(authClient.validateAdmin(new TokenRequest(token))).thenReturn(ResponseEntity.ok(null));

        // Act
        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(response.getContentAsString()).contains("Invalid Token or Unauthorized");

        verify(filterChain, never()).doFilter(request, response);
    }


    @Test
    void testDoFilterInternal_NoToken() throws ServletException, IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        when(cookieUtil.getCookieValue(request, ACCESS_TOKEN)).thenReturn(null);

        // Act
        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testShouldNotFilter_StaticResources() throws ServletException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/css/style.css");

        // Act
        boolean shouldNotFilter = tokenAuthenticationFilter.shouldNotFilter(request);

        // Assert
        assertThat(shouldNotFilter).isTrue();
    }

    @Test
    void testShouldNotFilter_LoginPage() throws ServletException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/login");

        // Act
        boolean shouldNotFilter = tokenAuthenticationFilter.shouldNotFilter(request);

        // Assert
        assertThat(shouldNotFilter).isTrue();
    }
}
