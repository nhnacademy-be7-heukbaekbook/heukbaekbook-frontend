package com.nhnacademy.heukbaekfrontend.oauth.service;

import com.nhnacademy.heukbaekfrontend.oauth.client.PaycoTokenClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaycoAuthorizationCodeTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private final PaycoTokenClient paycoTokenClient;

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        OAuth2AuthorizationExchange authorizationExchange = authorizationGrantRequest.getAuthorizationExchange();
        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();

        String grantType = AuthorizationGrantType.AUTHORIZATION_CODE.getValue();
        String code = authorizationExchange.getAuthorizationResponse().getCode();
        String redirectUri = clientRegistration.getRedirectUri();
        String clientId = clientRegistration.getClientId();
        String clientSecret = clientRegistration.getClientSecret();
        String state = authorizationExchange.getAuthorizationResponse().getState();

        Map<String, Object> responseBody = paycoTokenClient.getAccessToken(grantType, clientId, clientSecret, code, redirectUri, state);

        if (!responseBody.containsKey("access_token")) {
            throw new OAuth2AuthorizationException(new OAuth2Error("invalid_token_response",
                    "Token response does not contain access_token", null));
        }

        String accessToken = (String) responseBody.get("access_token");
        String refreshToken = (String) responseBody.get("refresh_token");
        long expiresIn = Long.parseLong((String) responseBody.get("expires_in"));

        return OAuth2AccessTokenResponse.withToken(accessToken)
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(expiresIn)
                .refreshToken(refreshToken)
                .build();
    }
}
