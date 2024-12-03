package com.nhnacademy.heukbaekfrontend.oauth.handler;

import com.nhnacademy.heukbaekfrontend.common.dto.LoginResponse;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.client.LoginClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberService memberService;
    private final LoginClient loginClient;
    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = authToken.getPrincipal();

        String idNo = extractIdNo(oAuth2User);
        if (idNo == null) {
            handleAuthenticationFailure(response, "ID 번호가 제공되지 않았습니다.");
            return;
        }

        if (isMemberExists(idNo)) {
            LoginResponse loginResponse = performLogin(idNo);
            if (loginResponse != null) {
                setSecurityContext(loginResponse);
                setCookies(response, loginResponse);
                response.sendRedirect("/");

                return;
            }

            handleAuthenticationFailure(response, "로그인 응답이 유효하지 않습니다.");
            return;
        }

        handleUserNotExist(response, oAuth2User);
    }

    private String extractIdNo(OAuth2User oAuth2User) {
        Map<String, Object> userAttributes = oAuth2User.getAttributes();
        return (String) userAttributes.get("idNo");
    }

    private boolean isMemberExists(String idNo) {
        ResponseEntity<Boolean> response = memberService.existsLoginId(idNo);
        return Boolean.TRUE.equals(response.getBody());
    }

    private LoginResponse performLogin(String idNo) {
        LoginRequest loginRequest = new LoginRequest(idNo, truncateTo10Chars(idNo));
        ResponseEntity<LoginResponse> loginResponseEntity = loginClient.login(loginRequest);
        return loginResponseEntity.getBody();
    }

    private String truncateTo10Chars(String idNo) {
        if (idNo != null && idNo.length() > 10) {
            return idNo.substring(0, 10);
        }
        return idNo;
    }

    private void setSecurityContext(LoginResponse loginResponse) {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(loginResponse.userRole()));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginResponse.userId(),
                null,
                authorities
        );
        authenticationToken.setDetails(loginResponse);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void setCookies(HttpServletResponse response, LoginResponse loginResponse) {
        cookieUtil.addCookie(response, "accessToken", loginResponse.accessToken(), loginResponse.accessExpire() / 1000);
        cookieUtil.addCookie(response, "refreshToken", loginResponse.refreshToken(), loginResponse.refreshExpire() / 1000);
    }

    private void handleAuthenticationFailure(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.sendRedirect("/login?error=true&message=" + message);
    }

    private void handleUserNotExist(HttpServletResponse response, OAuth2User oAuth2User) throws IOException {
        Map<String, Object> userAttributes = oAuth2User.getAttributes();

        String name = (String) userAttributes.get("name");
        String mobile = formatMobile((String) userAttributes.get("mobile"));
        String email = (String) userAttributes.get("email");
        String idNo = (String) userAttributes.get("idNo");

        if (idNo == null || idNo.isEmpty()) {
            handleAuthenticationFailure(response, "필수 정보인 ID 번호가 없습니다.");
            return;
        }

        addCookieIfNotNull(response, "name", name);
        addCookieIfNotNull(response, "phoneNumber", mobile);
        addCookieIfNotNull(response, "email", email);
        addCookieIfNotNull(response, "idNo", idNo);

        response.sendRedirect("/oauth2/additionalInfo");
    }

    private void addCookieIfNotNull(HttpServletResponse response, String name, String value) {
        if (value != null && !value.isEmpty()) {
            String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
            cookieUtil.addCookie(response, name, encodedValue, 300);
        }
    }

    private String formatMobile(String mobile) {
        if (mobile != null && mobile.startsWith("82") && mobile.length() == 12) {
            return "010-" + mobile.substring(4, 8) + "-" + mobile.substring(8);
        } else if (mobile != null && mobile.startsWith("010") && mobile.length() == 11) {
            return "010-" + mobile.substring(3, 7) + "-" + mobile.substring(7, 11);
        }
        return null;
    }
}
