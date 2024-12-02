package com.nhnacademy.heukbaekfrontend.oauth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.mockito.Mockito.*;

class CustomAuthenticationFailureHandlerTest {

    @Test
    void onAuthenticationFailure_ShouldRedirectToFailureUrl() throws IOException, ServletException {
        // Given
        CustomAuthenticationFailureHandler failureHandler = new CustomAuthenticationFailureHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException exception = mock(AuthenticationException.class);

        // Mock request and exception
        when(request.getContextPath()).thenReturn("");
        when(exception.getMessage()).thenReturn("Invalid credentials");

        // When
        failureHandler.onAuthenticationFailure(request, response, exception);

        // Then
        verify(response, times(1)).sendRedirect("/login?error=true");
        verify(exception, times(1)).getMessage();
    }
}
