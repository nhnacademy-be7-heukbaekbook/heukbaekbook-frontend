package com.nhnacademy.heukbaekfrontend.common.filter;

import com.nhnacademy.heukbaekfrontend.common.client.AuthClient;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.common.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReissueFilterTest {

    @Mock
    private AuthClient authClient;

    @Mock
    private CookieUtil cookieUtil;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private ReissueFilter reissueFilter;

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    @BeforeEach
    void setUp() {
        reissueFilter = new ReissueFilter(authClient, cookieUtil, jwtUtil);
    }

    @Test
    void testDoFilterInternal_withValidRefreshToken() throws ServletException, IOException {
        when(cookieUtil.getCookieValue(request, ACCESS_TOKEN)).thenReturn(null);
        when(cookieUtil.getCookieValue(request, REFRESH_TOKEN)).thenReturn("validRefreshToken");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, "accessToken=newAccessToken; Path=/; HttpOnly");
        ResponseEntity<String> responseEntity = ResponseEntity.ok().headers(headers).body(null);

        when(authClient.refreshTokens("refreshToken=validRefreshToken")).thenReturn(responseEntity);

        reissueFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<String> headerCaptor = ArgumentCaptor.forClass(String.class);
        verify(response, atLeastOnce()).addHeader(eq("Set-Cookie"), headerCaptor.capture());
        assertEquals("accessToken=newAccessToken; Path=/; HttpOnly", headerCaptor.getValue());

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_withNullAccessAndRefreshToken() throws ServletException, IOException {
        when(cookieUtil.getCookieValue(request, ACCESS_TOKEN)).thenReturn(null);
        when(cookieUtil.getCookieValue(request, REFRESH_TOKEN)).thenReturn(null);

        reissueFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_withInvalidRefreshToken() throws Exception {
        when(cookieUtil.getCookieValue(request, ACCESS_TOKEN)).thenReturn(null);
        when(cookieUtil.getCookieValue(request, REFRESH_TOKEN)).thenReturn("expiredRefreshToken");

        when(authClient.refreshTokens("refreshToken=expiredRefreshToken")).thenThrow(new RuntimeException("Invalid refresh token"));

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        reissueFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer, times(1)).write("Unauthorized: Invalid refresh token");
        verify(response, times(1)).sendRedirect("/login");
    }

    @Test
    void testDoFilterInternal_withValidAccessAndRefreshToken() throws ServletException, IOException {
        when(cookieUtil.getCookieValue(request, ACCESS_TOKEN)).thenReturn("validAccessToken");
        when(cookieUtil.getCookieValue(request, REFRESH_TOKEN)).thenReturn("validRefreshToken");

        reissueFilter.doFilterInternal(request, response, filterChain);

        verify(authClient, never()).refreshTokens(anyString());
        verify(response, never()).addHeader(eq("Set-Cookie"), anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

}
