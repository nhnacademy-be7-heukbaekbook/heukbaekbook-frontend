package com.nhnacademy.heukbaekfrontend.common.filter;

import com.nhnacademy.heukbaekfrontend.common.client.AuthClient;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.ACCESS_TOKEN;
import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.REFRESH_TOKEN;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthClient authClient;
    private final CookieUtil cookieUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = cookieUtil.getCookie(request, ACCESS_TOKEN);
        String refreshToken = cookieUtil.getCookie(request, REFRESH_TOKEN);

        try {
            if (accessToken == null && refreshToken != null) {
                ResponseEntity<String> refreshResponse = authClient.refreshTokens(REFRESH_TOKEN + "=" + refreshToken);
                Objects.requireNonNull(refreshResponse.getHeaders().get("Set-Cookie")).forEach(cookie -> response.addHeader("Set-Cookie", cookie));
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: " + e.getMessage());
            response.sendRedirect("/login");
        }
    }
}