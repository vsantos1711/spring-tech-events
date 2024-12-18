package com.vsantos.springtechevents.domain.coupon;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record CouponResponseDTO(UUID id, String code, Integer discount, OffsetDateTime valid, UUID eventId) {

}
