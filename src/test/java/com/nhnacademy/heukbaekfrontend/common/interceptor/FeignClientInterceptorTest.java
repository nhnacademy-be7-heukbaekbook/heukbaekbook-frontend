package com.nhnacademy.heukbaekfrontend.common.interceptor;

import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeignClientInterceptorTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private FeignClientInterceptor feignClientInterceptor;

    private RequestTemplate requestTemplate;

    @BeforeEach
    void setUp() {
        requestTemplate = new RequestTemplate();
    }

    @Test
    void apply_withAccessTokenAndRefreshToken() {
        Cookie accessTokenCookie = new Cookie("accessToken", "testAccessToken");
        Cookie refreshTokenCookie = new Cookie("refreshToken", "testRefreshToken");
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[]{accessTokenCookie, refreshTokenCookie});

        feignClientInterceptor.apply(requestTemplate);

        assertEquals("Bearer testAccessToken", requestTemplate.headers().get(HttpHeaders.AUTHORIZATION).iterator().next());
        assertEquals("refreshToken=testRefreshToken", requestTemplate.headers().get(HttpHeaders.COOKIE).iterator().next());
    }

    @Test
    void apply_withNoCookies() {
        when(httpServletRequest.getCookies()).thenReturn(null);

        feignClientInterceptor.apply(requestTemplate);

        assertFalse(requestTemplate.headers().containsKey(HttpHeaders.AUTHORIZATION));
        assertFalse(requestTemplate.headers().containsKey(HttpHeaders.COOKIE));
    }

    @Test
    void apply_withOnlyAccessToken() {
        Cookie accessTokenCookie = new Cookie("accessToken", "testAccessToken");
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[]{accessTokenCookie});

        feignClientInterceptor.apply(requestTemplate);

        assertEquals("Bearer testAccessToken", requestTemplate.headers().get(HttpHeaders.AUTHORIZATION).iterator().next());
        assertFalse(requestTemplate.headers().containsKey(HttpHeaders.COOKIE));
    }

    @Test
    void apply_withOnlyRefreshToken() {
        Cookie refreshTokenCookie = new Cookie("refreshToken", "testRefreshToken");
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[]{refreshTokenCookie});

        feignClientInterceptor.apply(requestTemplate);

        assertEquals("refreshToken=testRefreshToken", requestTemplate.headers().get(HttpHeaders.COOKIE).iterator().next());
        assertFalse(requestTemplate.headers().containsKey(HttpHeaders.AUTHORIZATION));
    }
}
