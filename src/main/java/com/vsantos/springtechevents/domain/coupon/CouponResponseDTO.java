package com.vsantos.springtechevents.domain.coupon;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record CouponResponseDTO(UUID id, String code, Integer discount, LocalDateTime valid, UUID eventId) {

}
