package com.nhnacademy.heukbaekfrontend.common.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    private static final String ACCESS_TOKEN_PREFIX = "Bearer ";
    private static final String X_USER_ID = "X-USER-ID";
    private static final String X_USER_ROLE = "X-USER-ROLE";

    private final HttpServletRequest httpServletRequest;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String accessToken = extractTokenFromCookie(httpServletRequest, ACCESS_TOKEN);
        String refreshToken = extractTokenFromCookie(httpServletRequest, REFRESH_TOKEN);

        // Access token 설정
        if (accessToken != null) {
            requestTemplate.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + accessToken);
        }

        // Refresh token 설정
        if (refreshToken != null) {
            requestTemplate.header(HttpHeaders.COOKIE, REFRESH_TOKEN + "=" + refreshToken);
        }

        // id와 role 설정
        Long userId = (Long) httpServletRequest.getAttribute("id");
        String userRole = (String) httpServletRequest.getAttribute("role");

        if (userId != null) {
            requestTemplate.header(X_USER_ID, userId.toString());
        }
        if (userRole != null) {
            requestTemplate.header(X_USER_ROLE, userRole);
        }
    }

    private String extractTokenFromCookie(HttpServletRequest request, String tokenName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(tokenName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
