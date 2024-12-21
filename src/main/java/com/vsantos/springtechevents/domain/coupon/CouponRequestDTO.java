package com.vsantos.springtechevents.domain.coupon;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CouponRequestDTO(

                @Schema(example = "XLR8")
                @NotBlank(message = "The coupon code is required")
                String code,

                @Schema(example = "1000")
                @NotNull(message = "Discount value is required")
                @Min(value = 0, message = "Discount value must be at least 0")
                Integer discount,

                @Schema(example = "2024-12-18T22:26:24.649Z")
                @NotNull(message = "Valid date is required")
                @FutureOrPresent(message = "Valid date must be in the present or future")
                OffsetDateTime valid) {
}