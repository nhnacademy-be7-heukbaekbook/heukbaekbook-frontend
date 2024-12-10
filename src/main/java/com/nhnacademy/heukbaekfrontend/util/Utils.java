package com.nhnacademy.heukbaekfrontend.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.util.UriComponentsBuilder;

public class Utils {

    private Utils() {}

    public static String getRedirectUrl(Integer page, Integer size, String sort, String uri) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(uri);
        if (page != null) {
            uriBuilder.queryParam("page", page);
        }
        if (size != null) {
            uriBuilder.queryParam("size", size);
        }
        if (sort != null && !sort.isEmpty()) {
            uriBuilder.queryParam("sort", sort.replace(": ", ","));
        }

        return uriBuilder.toUriString();
    }

    public static String getCustomerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                userId = userDetails.getUsername();
            } else {
                userId = principal.toString();
            }
        }
        return userId;
    }
}
