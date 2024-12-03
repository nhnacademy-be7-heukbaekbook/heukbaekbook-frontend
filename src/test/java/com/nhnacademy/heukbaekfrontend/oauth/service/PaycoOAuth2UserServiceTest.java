package com.nhnacademy.heukbaekfrontend.oauth.service;

import com.nhnacademy.heukbaekfrontend.oauth.client.PaycoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PaycoOAuth2UserServiceTest {

    private PaycoClient paycoClient;
    private PaycoOAuth2UserService paycoOAuth2UserService;

    @BeforeEach
    void setUp() {
        paycoClient = mock(PaycoClient.class);
        paycoOAuth2UserService = new PaycoOAuth2UserService(paycoClient);
    }

    @Test
    void loadUser_ShouldReturnOAuth2User_WhenResponseIsValid() {
        // Arrange
        Map<String, Object> response = createSuccessfulResponse();
        when(paycoClient.getUserInfo(anyString(), anyString())).thenReturn(response);

        OAuth2UserRequest userRequest = createOAuth2UserRequest();

        // Act
        OAuth2User user = paycoOAuth2UserService.loadUser(userRequest);

        // Assert
        assertThat(user).isNotNull();
        assertThat(user.getAttributes()).containsEntry("idNo", "12345");
        assertThat(user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MEMBER"))).isTrue();
    }

    @Test
    void loadUser_ShouldThrowException_WhenResponseIsUnsuccessful() {
        // Arrange
        Map<String, Object> response = createUnsuccessfulResponse();
        when(paycoClient.getUserInfo(anyString(), anyString())).thenReturn(response);

        OAuth2UserRequest userRequest = createOAuth2UserRequest();

        // Act & Assert
        OAuth2AuthenticationException exception = assertThrows(
                OAuth2AuthenticationException.class,
                () -> paycoOAuth2UserService.loadUser(userRequest)
        );

        assertThat(exception.getError().getErrorCode()).isEqualTo("invalid_response");
    }

    @Test
    void loadUser_ShouldThrowException_WhenResponseIsMalformed() {
        // Arrange
        Map<String, Object> response = createMalformedResponse();
        when(paycoClient.getUserInfo(anyString(), anyString())).thenReturn(response);

        OAuth2UserRequest userRequest = createOAuth2UserRequest();

        // Act & Assert
        assertThrows(ClassCastException.class, () -> paycoOAuth2UserService.loadUser(userRequest));
    }

    private OAuth2UserRequest createOAuth2UserRequest() {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("payco")
                .clientId("testClientId")
                .clientSecret("testClientSecret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost/callback")
                .authorizationUri("http://localhost/authorize") // authorizationUri 설정
                .tokenUri("http://localhost/token")
                .build();

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                "testAccessToken",
                Instant.now(),
                Instant.now().plusSeconds(3600)
        );

        return new OAuth2UserRequest(clientRegistration, accessToken);
    }

    private Map<String, Object> createSuccessfulResponse() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> header = new HashMap<>();
        header.put("isSuccessful", true);
        response.put("header", header);

        Map<String, Object> member = new HashMap<>();
        member.put("idNo", "12345");

        Map<String, Object> data = new HashMap<>();
        data.put("member", member);

        response.put("data", data);
        return response;
    }

    private Map<String, Object> createUnsuccessfulResponse() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> header = new HashMap<>();
        header.put("isSuccessful", false);
        response.put("header", header);
        return response;
    }

    private Map<String, Object> createMalformedResponse() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> header = new HashMap<>();
        header.put("isSuccessful", true);
        response.put("header", header);

        Map<String, Object> data = new HashMap<>();
        data.put("member", "invalid_member_data");

        response.put("data", data);
        return response;
    }
}
