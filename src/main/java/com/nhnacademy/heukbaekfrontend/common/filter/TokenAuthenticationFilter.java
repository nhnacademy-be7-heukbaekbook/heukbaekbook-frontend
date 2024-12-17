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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.nhnacademy.heukbaekfrontend.common.interceptor.FeignClientInterceptor.ACCESS_TOKEN;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthClient authClient;
    private final CookieUtil cookieUtil;

    private TokenResponse validateToken(String token) {
        TokenResponse response = validateMemberToken(token);
        if (response != null) {
            return response;
        }
        return validateAdminToken(token);
    }

    private TokenResponse validateMemberToken(String token) {
        try {
            return authClient.validateMember(new TokenRequest(token)).getBody();
        } catch (FeignException e) {
            return null;
        }
    }

    private TokenResponse validateAdminToken(String token) {
        try {
            return authClient.validateAdmin(new TokenRequest(token)).getBody();
        } catch (FeignException e) {
            if (e.status() == HttpServletResponse.SC_UNAUTHORIZED) {
                return null;
            }
            throw e;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = cookieUtil.getCookieValue(request, ACCESS_TOKEN);

        if (token != null &&
                (SecurityContextHolder.getContext().getAuthentication() == null ||
                        SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2AuthenticationToken)) {

            TokenResponse tokenResponse = validateToken(token);
            if (tokenResponse == null) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Invalid Token or Unauthorized\"}");
                return;
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            tokenResponse.id(),
                            null,
                            List.of(new SimpleGrantedAuthority(tokenResponse.role()))
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
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
                path.equals("/login") ||
                path.equals("/admin/login") ||
                path.startsWith("/signup") ||
                path.startsWith("/error");
    }
}
