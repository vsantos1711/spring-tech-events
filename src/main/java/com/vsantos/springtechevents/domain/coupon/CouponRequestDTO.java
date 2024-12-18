package com.vsantos.springtechevents.domain.coupon;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record CouponRequestDTO(
    @Schema(example = "XLR8") String code,
    @Schema(example = "1000") Integer discount,
    @Schema(example = "2024-12-18T22:26:24.649Z") OffsetDateTime valid) {

}