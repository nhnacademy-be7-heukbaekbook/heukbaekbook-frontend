package com.nhnacademy.heukbaekfrontend.common.interceptor;

import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final CookieUtil cookieUtil;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String accessToken = cookieUtil.getCookie(httpServletRequest, ACCESS_TOKEN);
        String refreshToken = cookieUtil.getCookie(httpServletRequest, REFRESH_TOKEN);

        // Access token 설정
        if (accessToken != null) {
            requestTemplate.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + accessToken);
        }

        // Refresh token 설정
        if (refreshToken != null) {
            requestTemplate.header(HttpHeaders.COOKIE, REFRESH_TOKEN + "=" + refreshToken);
        }

        // id와 role 설정
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Long userId) {
                requestTemplate.header(X_USER_ID, userId.toString());
            }

            authentication.getAuthorities().stream().findFirst().ifPresent(authority -> {
                requestTemplate.header(X_USER_ROLE, authority.getAuthority());
            });
        }
    }
}
