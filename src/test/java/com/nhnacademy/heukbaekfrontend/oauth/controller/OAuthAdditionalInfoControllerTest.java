package com.nhnacademy.heukbaekfrontend.oauth.controller;

import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberStatus;
import com.nhnacademy.heukbaekfrontend.oauth.dto.AdditionalInfoCreate;
import com.nhnacademy.heukbaekfrontend.oauth.service.OAuthAdditionalInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OAuthAdditionalInfoController.class)
class OAuthAdditionalInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OAuthAdditionalInfoService oAuthAdditionalInfoService;

    @MockBean
    private CookieUtil cookieUtil;

    @Test
    @WithMockUser // Mock 인증된 사용자 추가
    void showAdditionalInfoForm_ShouldReturnAdditionalInfoView() throws Exception {
        AdditionalInfoCreate additionalInfoCreate = new AdditionalInfoCreate(
                "123456789",
                "John Doe",
                "010-1234-5678",
                "john.doe@example.com",
                Date.valueOf("1990-01-01")
        );

        when(oAuthAdditionalInfoService.createAdditionalInfoForm(any())).thenReturn(additionalInfoCreate);

        mockMvc.perform(get("/oauth2/additionalInfo"))
                .andExpect(status().isOk())
                .andExpect(view().name("additionalInfo"))
                .andExpect(model().attributeExists("additionalInfoCreate"));

        verify(cookieUtil, times(1)).deleteAllCookies(any(), any());
    }

    @Test
    @WithMockUser
    void doSignupOAuth_ValidData_ShouldRedirectToHome() throws Exception {
        AdditionalInfoCreate additionalInfoCreate = new AdditionalInfoCreate(
                "1234567890", // 유효한 길이의 ID 번호
                "John Doe",
                "010-1234-5678",
                "john.doe@example.com",
                Date.valueOf("1990-01-01")
        );

        MemberResponse memberResponse = new MemberResponse(
                "John Doe",
                "010-1234-5678",
                "john.doe@example.com",
                "john123",
                Date.valueOf("1990-01-01"),
                null,
                null,
                MemberStatus.ACTIVE,
                null
        );

        when(oAuthAdditionalInfoService.signupOAuth(any())).thenReturn(Optional.of(memberResponse));

        mockMvc.perform(post("/oauth2/additionalInfo")
                        .param("idNo", additionalInfoCreate.idNo())
                        .param("name", additionalInfoCreate.name())
                        .param("phoneNumber", additionalInfoCreate.phoneNumber())
                        .param("email", additionalInfoCreate.email())
                        .param("birth", additionalInfoCreate.birth().toString())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // CSRF 토큰 추가
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(oAuthAdditionalInfoService, times(1)).processLogin(any(), any(), any());
    }

    @Test
    @WithMockUser
    void doSignupOAuth_InvalidData_ShouldReturnAdditionalInfoView() throws Exception {
        mockMvc.perform(post("/oauth2/additionalInfo")
                        .param("idNo", "") // 빈 값으로 ID 유효성 테스트
                        .param("name", "")
                        .param("phoneNumber", "")
                        .param("email", "invalid-email")
                        .param("birth", "2023-12-01")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("additionalInfo"))
                .andExpect(model().attributeExists("additionalInfoCreate"))
                .andExpect(model().attributeHasFieldErrors(
                        "additionalInfoCreate", "idNo", "name", "phoneNumber", "email"
                )); // birth 오류를 제거
    }

    @Test
    @WithMockUser
    void doSignupOAuth_MemberResponseEmpty_ShouldReturnErrorView() throws Exception {
        AdditionalInfoCreate additionalInfoCreate = new AdditionalInfoCreate(
                "123456789",
                "John Doe",
                "010-1234-5678",
                "john.doe@example.com",
                Date.valueOf("1990-01-01")
        );

        when(oAuthAdditionalInfoService.signupOAuth(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/oauth2/additionalInfo")
                        .param("idNo", additionalInfoCreate.idNo())
                        .param("name", additionalInfoCreate.name())
                        .param("phoneNumber", additionalInfoCreate.phoneNumber())
                        .param("email", additionalInfoCreate.email())
                        .param("birth", additionalInfoCreate.birth().toString())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // CSRF 토큰 추가
                .andExpect(status().isOk())
                .andExpect(view().name("additionalInfo"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("additionalInfoCreate"));
    }
}
