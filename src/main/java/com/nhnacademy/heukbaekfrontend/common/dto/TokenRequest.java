package com.nhnacademy.heukbaekfrontend.common.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(@NotBlank String accessToken) {
}
