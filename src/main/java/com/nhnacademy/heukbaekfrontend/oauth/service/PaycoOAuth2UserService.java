package com.nhnacademy.heukbaekfrontend.oauth.service;

import com.nhnacademy.heukbaekfrontend.oauth.client.PaycoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PaycoOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final PaycoClient paycoClient;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String clientId = userRequest.getClientRegistration().getClientId();
        String accessToken = userRequest.getAccessToken().getTokenValue();

        Map<String, Object> response = paycoClient.getUserInfo(clientId, accessToken);
        Map<String, Object> userAttributes = extractUserAttributes(response);

        return new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MEMBER")),
                userAttributes,
                "idNo"
        );
    }

    private Map<String, Object> extractUserAttributes(Map<String, Object> response) {
        Map<String, Object> header = (Map<String, Object>) response.get("header");

        if (!(boolean) header.get("isSuccessful")) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_response"), "PAYCO 응답이 성공적이지 않습니다.");
        }

        Map<String, Object> data = (Map<String, Object>) response.get("data");
        return (Map<String, Object>) data.get("member");
    }
}

