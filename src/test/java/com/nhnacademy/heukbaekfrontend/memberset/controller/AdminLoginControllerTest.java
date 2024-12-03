package com.nhnacademy.heukbaekfrontend.memberset.controller;

import com.nhnacademy.heukbaekfrontend.common.client.AuthClient;
import com.nhnacademy.heukbaekfrontend.common.config.TestConfig;
import com.nhnacademy.heukbaekfrontend.common.util.JwtUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.controller.AdminLoginController;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
import com.nhnacademy.heukbaekfrontend.memberset.member.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestConfig.class)
@WebMvcTest(AdminLoginController.class)
class AdminLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @MockBean
    private SecurityFilterChain securityFilterChain;



    @Test
    void testGetAdminLoginForm() throws Exception {
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/adminLogin"));
    }
}
