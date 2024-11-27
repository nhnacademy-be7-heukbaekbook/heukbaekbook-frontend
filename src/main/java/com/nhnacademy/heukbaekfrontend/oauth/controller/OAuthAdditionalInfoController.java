package com.nhnacademy.heukbaekfrontend.oauth.controller;

import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.oauth.dto.AdditionalInfoCreate;
import com.nhnacademy.heukbaekfrontend.oauth.service.OAuthAdditionalInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OAuthAdditionalInfoController {

    private final OAuthAdditionalInfoService oAuthAdditionalInfoService;
    private final CookieUtil cookieUtil;

    @GetMapping("/oauth2/additionalInfo")
    public String showAdditionalInfoForm(HttpServletRequest request, HttpServletResponse response, Model model) {
        AdditionalInfoCreate additionalInfoCreate = oAuthAdditionalInfoService.createAdditionalInfoForm(request);
        model.addAttribute("additionalInfoCreate", additionalInfoCreate);
        cookieUtil.deleteAllCookies(request, response);

        return "additionalInfo";
    }

    @PostMapping("/oauth2/additionalInfo")
    public String doSignupOAuth(@Valid AdditionalInfoCreate additionalInfoCreate,
                                BindingResult bindingResult,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("additionalInfoCreate", additionalInfoCreate);
            return "additionalInfo";
        }

        Optional<MemberResponse> memberResponse = oAuthAdditionalInfoService.signupOAuth(additionalInfoCreate);
        if (memberResponse.isEmpty()) {
            model.addAttribute("error", "회원 가입에 실패했습니다. 다시 시도해주세요.");
            model.addAttribute("additionalInfoCreate", additionalInfoCreate);
            return "additionalInfo";
        }

        oAuthAdditionalInfoService.processLogin(additionalInfoCreate.idNo(), additionalInfoCreate.idNo().substring(0, 10), response);

        return "redirect:/";
    }
}
