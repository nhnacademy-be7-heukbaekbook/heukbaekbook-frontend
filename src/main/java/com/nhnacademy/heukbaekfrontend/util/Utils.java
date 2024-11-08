package com.nhnacademy.heukbaekfrontend.util;

import org.springframework.web.util.UriComponentsBuilder;

public class Utils {

    public static String getRedirectUrl(Integer page, Integer size, String sort, String uri) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(uri);
        if (page != null) {
            uriBuilder.queryParam("page", page);
        }
        if (size != null) {
            uriBuilder.queryParam("size", size);
        }
        if (sort != null && !sort.isEmpty()) {
            uriBuilder.queryParam("sort", sort.replaceAll(": ", ","));
        }

        return uriBuilder.toUriString();
    }
}
