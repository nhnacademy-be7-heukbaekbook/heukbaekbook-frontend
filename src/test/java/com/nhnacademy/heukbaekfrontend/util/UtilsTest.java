package com.nhnacademy.heukbaekfrontend.util;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UtilsTest {

    @Test
    void testGetRedirectUrl() {
        String uri = "/test-uri";
        Integer page = 1;
        Integer size = 10;
        String sort = "name: asc";

        String expectedUrl = "/test-uri?page=1&size=10&sort=name,asc";

        String actualUrl = Utils.getRedirectUrl(page, size, sort, uri);

        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    void testGetRedirectUrl_NoParams() {
        String uri = "/test-uri";

        String expectedUrl = "/test-uri";

        String actualUrl = Utils.getRedirectUrl(null, null, null, uri);

        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    void testGetCustomerId_WithUserDetails() {
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(userDetails.getUsername()).thenReturn("testUser");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            String customerId = Utils.getCustomerId();

            assertThat(customerId).isEqualTo("testUser");
        }
    }

    @Test
    void testGetCustomerId_WithStringPrincipal() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(authentication.getPrincipal()).thenReturn("stringPrincipal");
        when(authentication.isAuthenticated()).thenReturn(true);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            String customerId = Utils.getCustomerId();

            assertThat(customerId).isEqualTo("stringPrincipal");
        }
    }

    @Test
    void testGetCustomerId_NoAuthentication() {
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(null);

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            String customerId = Utils.getCustomerId();

            assertThat(customerId).isNull();
        }
    }
}
