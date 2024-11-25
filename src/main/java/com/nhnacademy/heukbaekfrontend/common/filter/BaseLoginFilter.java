package com.nhnacademy.heukbaekfrontend.common.filter;

import com.nhnacademy.heukbaekfrontend.common.dto.LoginResponse;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public abstract class BaseLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final CookieUtil cookieUtil;
    private final AntPathRequestMatcher requiresAuthenticationRequestMatcher;

    protected BaseLoginFilter(AuthenticationManager authenticationManager, CookieUtil cookieUtil, String filterUrl) {
        super(authenticationManager);
        this.cookieUtil = cookieUtil;
        this.requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(filterUrl, "POST");
        setFilterProcessesUrl(filterUrl);
    }

    protected abstract ResponseEntity<LoginResponse> performLogin(LoginRequest loginRequest);

    protected abstract String getSuccessRedirectUrl();

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return requiresAuthenticationRequestMatcher.matches(request);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            String loginId = request.getParameter("loginId");
            String password = request.getParameter("password");

            if (loginId == null || password == null) {
                throw new BadCredentialsException("아이디, 비밀번호가 필요합니다.");
            }

            LoginRequest loginRequest = new LoginRequest(loginId, password);
            ResponseEntity<LoginResponse> login = performLogin(loginRequest);

            LoginResponse loginResponse = Objects.requireNonNull(login.getBody(), "로그인 응답이 없습니다.");

            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(loginResponse.userRole()));

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginResponse.userId(),
                    null,
                    authorities
            );
            authenticationToken.setDetails(loginResponse);

            return authenticationToken;

        } catch (Exception e) {
            throw new AuthenticationServiceException("로그인 요청 실패", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);

        LoginResponse loginResponse = (LoginResponse) authResult.getDetails();

        if (loginResponse != null) {
            cookieUtil.addCookie(response, "accessToken", loginResponse.accessToken(), loginResponse.accessExpire() / 1000);
            cookieUtil.addCookie(response, "refreshToken", loginResponse.refreshToken(), loginResponse.refreshExpire() / 1000);
        }

        response.sendRedirect(getSuccessRedirectUrl());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
