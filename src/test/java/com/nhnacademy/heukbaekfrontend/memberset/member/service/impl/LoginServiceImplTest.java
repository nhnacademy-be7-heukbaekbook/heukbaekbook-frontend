package com.nhnacademy.heukbaekfrontend.memberset.member.service.impl;

import com.nhnacademy.heukbaekfrontend.memberset.member.client.LoginClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class LoginServiceImplTest {

    private LoginServiceImpl loginService;
    private LoginClient loginClient;
    private HttpServletResponse response;

    @BeforeEach
    void setup() {
        loginClient = mock(LoginClient.class);
        loginService = new LoginServiceImpl(loginClient);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void testLogin_AlwaysReturnsTrue() {
        // Given
        LoginRequest loginRequest = new LoginRequest("user", "password");

        // When
        boolean result = loginService.login(loginRequest, response);

        // Then
        assertTrue(result, "The login method should always return true.");
        verifyNoInteractions(loginClient, response);
    }

    @Test
    void testAdminLogin_AlwaysReturnsTrue() {
        // Given
        LoginRequest loginRequest = new LoginRequest("admin", "adminpassword");

        // When
        boolean result = loginService.adminLogin(loginRequest, response);

        // Then
        assertTrue(result, "The adminLogin method should always return true.");
        verifyNoInteractions(loginClient, response);
    }
}
