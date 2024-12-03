package com.nhnacademy.heukbaekfrontend.oauth.service;

import com.nhnacademy.heukbaekfrontend.oauth.client.PaycoTokenClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PaycoAuthorizationCodeTokenResponseClientTest {

    private PaycoTokenClient paycoTokenClient;
    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> client;

    @BeforeEach
    void setUp() {
        paycoTokenClient = mock(PaycoTokenClient.class);
        client = new PaycoAuthorizationCodeTokenResponseClient(paycoTokenClient);
    }

    @Test
    void getTokenResponse_ShouldReturnAccessTokenResponse() {
        // Arrange
        Map<String, Object> tokenResponse = new HashMap<>();
        tokenResponse.put("access_token", "testAccessToken");
        tokenResponse.put("refresh_token", "testRefreshToken");
        tokenResponse.put("expires_in", "3600");

        when(paycoTokenClient.getAccessToken(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(tokenResponse);

        OAuth2AuthorizationCodeGrantRequest request = createAuthorizationCodeGrantRequest();

        // Act
        OAuth2AccessTokenResponse response = client.getTokenResponse(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken().getTokenValue()).isEqualTo("testAccessToken");

        // 리프레시 토큰 값 비교
        assertThat(response.getRefreshToken()).isNotNull();
        assertThat(response.getRefreshToken().getTokenValue()).isEqualTo("testRefreshToken");

        assertThat(response.getAccessToken().getExpiresAt()).isNotNull();
    }


    @Test
    void getTokenResponse_ShouldThrowException_WhenAccessTokenMissing() {
        // Arrange
        Map<String, Object> tokenResponse = new HashMap<>();
        tokenResponse.put("refresh_token", "testRefreshToken");
        tokenResponse.put("expires_in", "3600");

        when(paycoTokenClient.getAccessToken(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(tokenResponse);

        OAuth2AuthorizationCodeGrantRequest request = createAuthorizationCodeGrantRequest();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> client.getTokenResponse(request));
    }

    private OAuth2AuthorizationCodeGrantRequest createAuthorizationCodeGrantRequest() {
        String authorizationUri = "http://localhost/authorize"; // 명시적으로 URL 지정
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("payco")
                .clientId("testClientId")
                .clientSecret("testClientSecret")
                .redirectUri("http://localhost/callback")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri(authorizationUri) // authorizationUri 명시적으로 추가
                .tokenUri("http://localhost/token")
                .build();

        OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
                .clientId(clientRegistration.getClientId())
                .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri()) // 수정된 부분
                .redirectUri(clientRegistration.getRedirectUri())
                .state("testState")
                .build();

        OAuth2AuthorizationResponse authorizationResponse = OAuth2AuthorizationResponse.success("testCode")
                .state("testState")
                .redirectUri(clientRegistration.getRedirectUri())
                .build();

        OAuth2AuthorizationExchange authorizationExchange = new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse);

        return new OAuth2AuthorizationCodeGrantRequest(clientRegistration, authorizationExchange);
    }

}
