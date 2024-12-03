package com.nhnacademy.heukbaekfrontend.common.filter;

import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.common.dto.LoginResponse;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.client.LoginClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AdminLoginFilterTest {

    private AdminLoginFilter adminLoginFilter;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private LoginClient loginClient;

    @Mock
    private CookieUtil cookieUtil;

    @Mock
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminLoginFilter = new AdminLoginFilter(authenticationManager, loginClient, cookieUtil, cartService);
    }

    @Test
    void testPerformLogin() {
        // Arrange
        LoginRequest mockLoginRequest = new LoginRequest("admin", "password");
        LoginResponse mockLoginResponse = new LoginResponse(
                1L, "ADMIN", "access-token", "refresh-token", 3600L, 7200L
        );

        when(loginClient.adminLogin(mockLoginRequest)).thenReturn(ResponseEntity.ok(mockLoginResponse));

        // Act
        ResponseEntity<LoginResponse> response = adminLoginFilter.performLogin(mockLoginRequest);

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().userRole()).isEqualTo("ADMIN");
        assertThat(response.getBody().accessToken()).isEqualTo("access-token");
        verify(loginClient, times(1)).adminLogin(mockLoginRequest);
    }

    @Test
    void testGetSuccessRedirectUrl() {
        // Act
        String redirectUrl = adminLoginFilter.getSuccessRedirectUrl();

        // Assert
        assertThat(redirectUrl).isEqualTo("/admin/home");
    }
}
