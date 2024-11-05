package com.nhnacademy.heukbaekfrontend.member.controller;

import com.nhnacademy.heukbaekfrontend.member.dto.LoginRequest;
import com.nhnacademy.heukbaekfrontend.member.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;


    @Test
    void testGetLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("/login/login"));
    }

    @Test
    void testDoLogin_Success() throws Exception {
        when(loginService.login(any(LoginRequest.class), any(HttpServletResponse.class))).thenReturn(true);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "user")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(loginService).login(any(LoginRequest.class), any(HttpServletResponse.class));
    }

    @Test
    void testDoLogin_Failure() throws Exception {
        when(loginService.login(any(LoginRequest.class), any(HttpServletResponse.class))).thenReturn(false);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "user")
                        .param("password", "wrongPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("/login/login"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요."));

        verify(loginService).login(any(LoginRequest.class), any(HttpServletResponse.class));
    }
}