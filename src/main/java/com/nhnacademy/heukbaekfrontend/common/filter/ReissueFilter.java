package com.nhnacademy.heukbaekfrontend.common.filter;

import com.nhnacademy.heukbaekfrontend.common.client.AuthClient;
import com.nhnacademy.heukbaekfrontend.common.filter.wrapper.MutableHttpServletRequest;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.common.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.ACCESS_TOKEN;
import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.REFRESH_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class ReissueFilter extends OncePerRequestFilter {
    private final AuthClient authClient;
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = cookieUtil.getCookieValue(request, ACCESS_TOKEN);
        String refreshToken = cookieUtil.getCookieValue(request, REFRESH_TOKEN);

        if ((accessToken == null || jwtUtil.isExpired(accessToken)) && refreshToken != null) {
            ResponseEntity<String> refreshResponse = authClient.refreshTokens(REFRESH_TOKEN + "=" + refreshToken);

            Objects.requireNonNull(refreshResponse.getHeaders().get("Set-Cookie"))
                    .forEach(cookie -> response.addHeader(HttpHeaders.SET_COOKIE, cookie));

            String newAccessToken = extractAccessTokenFromSetCookie(refreshResponse);
            if (newAccessToken != null) {
                Cookie[] existingCookies = request.getCookies();
                if (existingCookies == null) {
                    existingCookies = new Cookie[0];
                }

                Cookie newAccessTokenCookie = new Cookie(ACCESS_TOKEN, newAccessToken);
                newAccessTokenCookie.setPath("/");
                newAccessTokenCookie.setHttpOnly(true);

                Cookie[] newCookies = Arrays.copyOf(existingCookies, existingCookies.length + 1);
                newCookies[existingCookies.length] = newAccessTokenCookie;

                MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
                mutableRequest.setCookies(newCookies);

                filterChain.doFilter(mutableRequest, response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return path.startsWith("/css") ||
                path.startsWith("/images") ||
                path.startsWith("/js") ||
                path.startsWith("/favicon") ||
                path.startsWith("/error");
    }

    private String extractAccessTokenFromSetCookie(ResponseEntity<String> response) {
        return Objects.requireNonNull(response.getHeaders().get(HttpHeaders.SET_COOKIE)).stream()
                .filter(cookie -> cookie.startsWith(ACCESS_TOKEN + "="))
                .map(cookie -> {
                    int start = cookie.indexOf('=') + 1;
                    int end = cookie.indexOf(';');
                    return (end == -1) ? cookie.substring(start) : cookie.substring(start, end);
                })
                .findFirst()
                .orElse(null);
    }
}
