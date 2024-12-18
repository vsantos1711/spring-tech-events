package com.vsantos.springtechevents.domain.coupon;

import java.time.OffsetDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CouponResponseDTO(
    UUID id,
    @Schema(example = "XRL8") String code,
    @Schema(example = "1000") Integer discount,
    @Schema(example = "2024-12-18T22:26:24.649Z") OffsetDateTime valid,
    UUID eventId) {

}
