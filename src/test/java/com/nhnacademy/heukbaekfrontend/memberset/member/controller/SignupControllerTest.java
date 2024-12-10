package com.nhnacademy.heukbaekfrontend.memberset.member.controller;

import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberCreateRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SignupController.class)
class SignupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    private MemberCreateRequest validSignupRequest;

    @BeforeEach
    void setup() {
        validSignupRequest = new MemberCreateRequest(
                "john123",
                "Password1!",
                Date.valueOf(LocalDate.of(1990, 1, 1)),
                "홍길동",
                "010-1234-5678",
                "john.doe@example.com",
                12345L,
                "서울특별시 강남구",
                "101호",
                "우리집"
        );
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void testGetSignUpForm() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attributeExists("signupForm"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void testDoSignup_InvalidRequest() throws Exception {
        MemberCreateRequest invalidRequest = new MemberCreateRequest(
                "abc", // Invalid loginId
                "short", // Invalid password
                null, // Missing birth
                "J", // Invalid name
                "010-123", // Invalid phoneNumber
                "invalid-email", // Invalid email
                null, // Missing postalCode
                "", // Missing roadNameAddress
                "Too long address exceeding limit....", // Invalid detailAddress
                "invalidAlias123" // Invalid alias
        );

        mockMvc.perform(post("/signup")
                        .with(csrf())
                        .flashAttr("memberCreateRequest", invalidRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signup"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void testCheckLoginIdDuplicate_True() throws Exception {
        when(memberService.existsLoginId("existingId")).thenReturn(ResponseEntity.ok(true));

        mockMvc.perform(get("/signup/check-duplicate/loginId/existingId"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void testCheckLoginIdDuplicate_False() throws Exception {
        when(memberService.existsLoginId("newId")).thenReturn(ResponseEntity.ok(false));

        mockMvc.perform(get("/signup/check-duplicate/loginId/newId"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void testCheckEmailDuplicate_True() throws Exception {
        when(memberService.existsEmail("existing@example.com")).thenReturn(ResponseEntity.ok(true));

        mockMvc.perform(get("/signup/check-duplicate/email/existing@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void testCheckEmailDuplicate_False() throws Exception {
        when(memberService.existsEmail("new@example.com")).thenReturn(ResponseEntity.ok(false));

        mockMvc.perform(get("/signup/check-duplicate/email/new@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
