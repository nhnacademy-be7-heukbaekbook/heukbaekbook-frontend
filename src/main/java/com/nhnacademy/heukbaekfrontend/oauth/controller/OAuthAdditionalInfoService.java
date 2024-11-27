package com.nhnacademy.heukbaekfrontend.oauth.controller;

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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OAuthAdditionalInfoService {

    private final CookieUtil cookieUtil;
    private final MemberService memberService;
    private final LoginClient loginClient;

    @GetMapping("/oauth2/additionalInfo")
    public String showAdditionalInfoForm(HttpServletRequest request, Model model) {
        AdditionalInfoCreate additionalInfoCreate = createAdditionalInfoForm(request);
        model.addAttribute("additionalInfoCreate", additionalInfoCreate);
        return "additionalInfo";
    }

    @PostMapping("/oauth2/additionalInfo")
    public String doSignupOAuth(@Valid AdditionalInfoCreate additionalInfoCreate,
                                BindingResult bindingResult,
                                HttpServletResponse response,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("additionalInfoCreate", additionalInfoCreate);
            return "additionalInfo";
        }

        Optional<MemberResponse> memberResponse = signupOAuth(additionalInfoCreate);
        if (memberResponse.isEmpty()) {
            model.addAttribute("error", "회원 가입에 실패했습니다. 다시 시도해주세요.");
            model.addAttribute("additionalInfoCreate", additionalInfoCreate);
            return "additionalInfo";
        }

        processLogin(additionalInfoCreate.idNo(), additionalInfoCreate.idNo().substring(0, 10), response);

        return "redirect:/";
    }

    private AdditionalInfoCreate createAdditionalInfoForm(HttpServletRequest request) {
        return new AdditionalInfoCreate(
                decodeCookieValue(cookieUtil.getCookieValue(request, "idNo")),
                decodeCookieValue(cookieUtil.getCookieValue(request, "name")),
                decodeCookieValue(cookieUtil.getCookieValue(request, "phoneNumber")),
                decodeCookieValue(cookieUtil.getCookieValue(request, "email")),
                null
        );
    }

    private Optional<MemberResponse> signupOAuth(AdditionalInfoCreate additionalInfoCreate) {
        OAuthMemberCreateRequest createRequest = new OAuthMemberCreateRequest(
                additionalInfoCreate.idNo(),
                additionalInfoCreate.idNo().substring(0, 10), // 임시 비밀번호
                additionalInfoCreate.birth(),
                additionalInfoCreate.name(),
                additionalInfoCreate.phoneNumber(),
                additionalInfoCreate.email()
        );
        return memberService.signupOAuth(createRequest);
    }

    private void processLogin(String id, String password, HttpServletResponse response) {
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

