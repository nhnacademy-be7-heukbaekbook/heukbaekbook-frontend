package com.nhnacademy.heukbaekfrontend.oauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.sql.Date;

public record AdditionalInfoCreate(
        @NotBlank(message = "ID 번호는 필수 입력 항목입니다.")
        String idNo,

        @NotBlank(message = "이름은 필수 입력 항목입니다.")
        String name,

        @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
        String phoneNumber,

        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        @NotNull(message = "생년월일을 입력하여 주십시오.")
        @PastOrPresent(message = "올바른 생년월일 형식이 아닙니다.")
        Date birth
) {}
