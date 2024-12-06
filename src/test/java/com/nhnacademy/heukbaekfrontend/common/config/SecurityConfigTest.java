package com.nhnacademy.heukbaekfrontend.common.config;

import com.nhnacademy.heukbaekfrontend.cart.service.CartService;
import com.nhnacademy.heukbaekfrontend.common.client.AuthClient;
import com.nhnacademy.heukbaekfrontend.common.util.CookieUtil;
import com.nhnacademy.heukbaekfrontend.common.util.JwtUtil;
import com.nhnacademy.heukbaekfrontend.memberset.member.client.LoginClient;
import com.nhnacademy.heukbaekfrontend.memberset.member.client.LogoutClient;
import com.nhnacademy.heukbaekfrontend.oauth.client.PaycoClient;
import com.nhnacademy.heukbaekfrontend.oauth.client.PaycoTokenClient;
import com.nhnacademy.heukbaekfrontend.oauth.handler.CustomAuthenticationFailureHandler;
import com.nhnacademy.heukbaekfrontend.oauth.handler.CustomAuthenticationSuccessHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @Mock
    private LoginClient loginClient;

    @Mock
    private AuthClient authClient;

    @Mock
    private CookieUtil cookieUtil;

    @Mock
    private LogoutClient logoutClient;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PaycoClient paycoClient;

    @Mock
    private PaycoTokenClient paycoTokenClient;

    @Mock
    private CartService cartService;

    @Mock
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Mock
    private CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Mock
    private ClientRegistrationRepository clientRegistrationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityConfig = new SecurityConfig(
                loginClient,
                authClient,
                cookieUtil,
                logoutClient,
                jwtUtil,
                paycoClient,
                clientRegistrationRepository, // Mocked clientRegistrationRepository provided
                authenticationSuccessHandler,
                authenticationFailureHandler,
                paycoTokenClient,
                cartService
        );
    }

    @Test
    void testBCryptPasswordEncoder() {
        BCryptPasswordEncoder encoder = securityConfig.bCryptPasswordEncoder();
        assertThat(encoder).isNotNull();
        String encodedPassword = encoder.encode("password");
        assertThat(encoder.matches("password", encodedPassword)).isTrue();
    }

    @Test
    void testAuthenticationManager() throws Exception {
        var authenticationConfiguration = mock(org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration.class);
        var authenticationManager = mock(AuthenticationManager.class);

        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

        AuthenticationManager result = securityConfig.authenticationManager(authenticationConfiguration);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(authenticationManager);
    }

    @Test
    void testFilterChain() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        when(http.csrf(any())).thenReturn(http);
        when(http.formLogin(any())).thenReturn(http);
        when(http.httpBasic(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.oauth2Login(any())).thenReturn(http);
        when(http.logout(any())).thenReturn(http);

        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

        assertThat(securityConfig.filterChain(http, authenticationManager)).isNotNull();
    }

    @Test
    void testMemberLoginFilter() {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        var memberLoginFilter = securityConfig.memberLoginFilter(authenticationManager);
        assertThat(memberLoginFilter).isNotNull();
    }

    @Test
    void testAdminLoginFilter() {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        var adminLoginFilter = securityConfig.adminLoginFilter(authenticationManager);
        assertThat(adminLoginFilter).isNotNull();
    }

    @Test
    void testTokenAuthenticationFilter() {
        var tokenAuthenticationFilter = securityConfig.tokenAuthenticationFilter();
        assertThat(tokenAuthenticationFilter).isNotNull();
    }

    @Test
    void testReissueFilter() {
        var reissueFilter = securityConfig.reissueFilter();
        assertThat(reissueFilter).isNotNull();
    }

    @Test
    void testPaycoOAuth2UserService() {
        var oAuth2UserService = securityConfig.paycoOAuth2UserService();
        assertThat(oAuth2UserService).isNotNull();
    }

    @Test
    void testCustomAuthorizationRequestResolver() {
        var resolver = securityConfig.customAuthorizationRequestResolver();
        assertThat(resolver).isNotNull();
    }

    @Test
    void testPaycoAccessTokenResponseClient() {
        var responseClient = securityConfig.paycoAccessTokenResponseClient();
        assertThat(responseClient).isNotNull();
    }

    @Test
    void testHttpCookieOAuth2AuthorizationRequestRepository() {
        var repository = securityConfig.httpCookieOAuth2AuthorizationRequestRepository();
        assertThat(repository).isNotNull();
    }
}
