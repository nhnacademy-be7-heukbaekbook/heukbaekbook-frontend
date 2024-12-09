package com.nhnacademy.heukbaekfrontend.review.dto.response;

import java.util.List;

public record ReviewCreateResponse (String title, List<String> url) {
}
