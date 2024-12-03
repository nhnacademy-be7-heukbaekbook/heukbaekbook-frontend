package com.nhnacademy.heukbaekfrontend.oauth.service;

import com.nhnacademy.heukbaekfrontend.common.dto.LoginResponse;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.client.LoginClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import com.nhnacademy.heukbaekfrontend.oauth.dto.AdditionalInfoCreate;
import com.nhnacademy.heukbaekfrontend.oauth.dto.OAuthMemberCreateRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthAdditionalInfoService {
    private final CookieUtil cookieUtil;
    private final MemberService memberService;
    private final LoginClient loginClient;

    public AdditionalInfoCreate createAdditionalInfoForm(HttpServletRequest request) {
        return new AdditionalInfoCreate(
                decodeCookieValue(cookieUtil.getCookieValue(request, "idNo")),
                decodeCookieValue(cookieUtil.getCookieValue(request, "name")),
                decodeCookieValue(cookieUtil.getCookieValue(request, "phoneNumber")),
                decodeCookieValue(cookieUtil.getCookieValue(request, "email")),
                null
        );
    }

    public Optional<MemberResponse> signupOAuth(AdditionalInfoCreate additionalInfoCreate) {
        OAuthMemberCreateRequest createRequest = new OAuthMemberCreateRequest(
                additionalInfoCreate.idNo(),
                additionalInfoCreate.idNo().substring(0, 10),
                additionalInfoCreate.birth(),
                additionalInfoCreate.name(),
                additionalInfoCreate.phoneNumber(),
                additionalInfoCreate.email()
        );
        return memberService.signupOAuth(createRequest);
    }

    public void processLogin(String id, String password, HttpServletResponse response) {
        LoginResponse loginResponse = loginClient.login(new LoginRequest(id, password)).getBody();

        cookieUtil.addCookie(response, "accessToken", loginResponse.accessToken(), loginResponse.accessExpire() / 1000);
        cookieUtil.addCookie(response, "refreshToken", loginResponse.refreshToken(), loginResponse.refreshExpire() / 1000);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                loginResponse.userId(),
                null,
                List.of(new SimpleGrantedAuthority(loginResponse.userRole()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String decodeCookieValue(String value) {
        if (value != null) {
            return URLDecoder.decode(value, StandardCharsets.UTF_8);
        }
        return null;
    }
}
