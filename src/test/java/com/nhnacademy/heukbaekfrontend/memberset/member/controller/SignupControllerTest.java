package com.nhnacademy.heukbaekfrontend.memberset.member.controller;

import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberCreateRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.MemberResponse;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.MemberService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SignupController.class)
class SignupControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private CookieUtil cookieUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new SignupController(memberService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

//    @Test
//    void testGetSignUpForm() throws Exception {
//        mockMvc.perform(get("/signup"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("signupForm"))
//                .andExpect(view().name("signup"));
//    }
//
//    @Test
//    void testDoSignup_Success() throws Exception {
//        MemberResponse mockResponse = new MemberResponse(
//                "John Doe", "010-1234-5678", "john.doe@example.com",
//                "john_doe", null, null, null, null, null
//        );
//
//        when(memberService.signup(any(MemberCreateRequest.class)))
//                .thenReturn(Optional.of(mockResponse));
//
//        mockMvc.perform(post("/signup")
//                        .param("loginId", "john_doe")
//                        .param("password", "password1!")
//                        .param("birth", "1990-01-01")
//                        .param("name", "John Doe")
//                        .param("phoneNumber", "010-1234-5678")
//                        .param("email", "john.doe@example.com")
//                        .param("postalCode", "12345")
//                        .param("roadNameAddress", "Main Street 123")
//                        .param("detailAddress", "Apartment 45")
//                        .param("alias", "Home"))
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("name", mockResponse.name()))
//                .andExpect(view().name("signupSuccess"));
//    }

    @Test
    void testDoSignup_BindingError() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("loginId", "")
                        .param("password", "")
                        .param("birth", "")
                        .param("name", "")
                        .param("phoneNumber", "")
                        .param("email", "")
                        .param("postalCode", "")
                        .param("roadNameAddress", "")
                        .param("detailAddress", "")
                        .param("alias", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("signupForm"))
                .andExpect(flash().attributeExists("error"))
                .andExpect(redirectedUrl("/signup"));
    }

    @Test
    void testCheckLoginIdDuplicate() throws Exception {
        when(memberService.existsLoginId("john_doe"))
                .thenReturn(ResponseEntity.ok(true));

        mockMvc.perform(get("/signup/check-duplicate/loginId/john_doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testCheckEmailDuplicate() throws Exception {
        when(memberService.existsEmail("john.doe@example.com"))
                .thenReturn(ResponseEntity.ok(true));

        mockMvc.perform(get("/signup/check-duplicate/email/john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}

