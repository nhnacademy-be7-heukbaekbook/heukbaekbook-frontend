package com.nhnacademy.heukbaekfrontend.point.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record PointEarnStandardRequest(
        @NotNull
        @Length(max = 20)
        String name,

        @Min(0)
        @NotNull
        BigDecimal point,

        PointEarnStandardStatus status,

        @NotNull
        PointEarnTriggerEvent triggerEvent
) {
        public PointEarnStandardRequest {
                if (status == null) {
                        status = PointEarnStandardStatus.ACTIVATED;
                }
        }
}

