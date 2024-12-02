package com.nhnacademy.heukbaekfrontend.oauth.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomAuthorizationRequestResolverTest {

    private ClientRegistrationRepository clientRegistrationRepository;
    private CustomAuthorizationRequestResolver resolver;

    @BeforeEach
    void setUp() {
        clientRegistrationRepository = mock(ClientRegistrationRepository.class);
        resolver = new CustomAuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");

        // Mock ClientRegistration
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("test-client-id")
                .clientId("test-client-id")
                .authorizationUri("https://auth.example.com/authorize")
                .redirectUri("http://localhost/callback")
                .tokenUri("https://auth.example.com/token")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();
        when(clientRegistrationRepository.findByRegistrationId("test-client-id")).thenReturn(clientRegistration);
    }

    @Test
    void resolveWithClientRegistrationId_ShouldCustomizeAuthorizationRequest() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/oauth2/authorization/test-client-id");
        when(request.getScheme()).thenReturn("http");
        when(request.getServerName()).thenReturn("localhost");
        when(request.getServerPort()).thenReturn(8080);

        String clientRegistrationId = "test-client-id";

        // Act
        OAuth2AuthorizationRequest result = resolver.resolve(request, clientRegistrationId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAdditionalParameters()).containsEntry("serviceProviderCode", "FRIENDS");
        assertThat(result.getAdditionalParameters()).containsEntry("userLocale", "ko_KR");
    }

    @Test
    void resolve_ShouldReturnNull_WhenOriginalRequestIsNull() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/oauth2/authorization/unknown-client-id");
        when(request.getScheme()).thenReturn("http");
        when(request.getServerName()).thenReturn("localhost");
        when(request.getServerPort()).thenReturn(8080);

        // Act
        OAuth2AuthorizationRequest result = resolver.resolve(request);

        // Assert
        assertThat(result).isNull();
    }
}
