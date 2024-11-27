package com.nhnacademy.heukbaekfrontend.common.interceptor;

import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeignClientInterceptorTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private CookieUtil cookieUtil;

    @InjectMocks
    private FeignClientInterceptor feignClientInterceptor;

    private RequestTemplate requestTemplate;

    @BeforeEach
    void setUp() {
        requestTemplate = new RequestTemplate();
    }

    @Test
    void apply_withAccessTokenAndRefreshToken() {
        // Mock 쿠키 값 설정
        when(cookieUtil.getCookieValue(httpServletRequest, "accessToken")).thenReturn("testAccessToken");
        when(cookieUtil.getCookieValue(httpServletRequest, "refreshToken")).thenReturn("testRefreshToken");

        feignClientInterceptor.apply(requestTemplate);

        // AUTHORIZATION 헤더 검증
        assertTrue(requestTemplate.headers().containsKey(HttpHeaders.AUTHORIZATION));
        assertEquals("Bearer testAccessToken",
                requestTemplate.headers().get(HttpHeaders.AUTHORIZATION).iterator().next());

        // COOKIE 헤더 검증
        assertTrue(requestTemplate.headers().containsKey(HttpHeaders.COOKIE));
        assertEquals("refreshToken=testRefreshToken",
                requestTemplate.headers().get(HttpHeaders.COOKIE).iterator().next());
    }

    @Test
    void apply_withNoCookies() {
        // 쿠키가 없는 경우
        when(cookieUtil.getCookieValue(httpServletRequest, "accessToken")).thenReturn(null);
        when(cookieUtil.getCookieValue(httpServletRequest, "refreshToken")).thenReturn(null);

        feignClientInterceptor.apply(requestTemplate);

        // 헤더가 비어 있는지 확인
        assertFalse(requestTemplate.headers().containsKey(HttpHeaders.AUTHORIZATION));
        assertFalse(requestTemplate.headers().containsKey(HttpHeaders.COOKIE));
    }

    @Test
    void apply_withOnlyAccessToken() {
        // Mock Access Token 쿠키만 설정
        when(cookieUtil.getCookieValue(httpServletRequest, "accessToken")).thenReturn("testAccessToken");
        when(cookieUtil.getCookieValue(httpServletRequest, "refreshToken")).thenReturn(null);

        feignClientInterceptor.apply(requestTemplate);

        // AUTHORIZATION 헤더 검증
        assertTrue(requestTemplate.headers().containsKey(HttpHeaders.AUTHORIZATION));
        assertEquals("Bearer testAccessToken",
                requestTemplate.headers().get(HttpHeaders.AUTHORIZATION).iterator().next());

        // COOKIE 헤더가 없는지 확인
        assertFalse(requestTemplate.headers().containsKey(HttpHeaders.COOKIE));
    }

    @Test
    void apply_withOnlyRefreshToken() {
        // Mock Refresh Token 쿠키만 설정
        when(cookieUtil.getCookieValue(httpServletRequest, "accessToken")).thenReturn(null);
        when(cookieUtil.getCookieValue(httpServletRequest, "refreshToken")).thenReturn("testRefreshToken");

        feignClientInterceptor.apply(requestTemplate);

        // COOKIE 헤더 검증
        assertTrue(requestTemplate.headers().containsKey(HttpHeaders.COOKIE));
        assertEquals("refreshToken=testRefreshToken",
                requestTemplate.headers().get(HttpHeaders.COOKIE).iterator().next());

        // AUTHORIZATION 헤더가 없는지 확인
        assertFalse(requestTemplate.headers().containsKey(HttpHeaders.AUTHORIZATION));
    }
}
