package com.vsantos.springtechevents.domain.event;

import java.util.List;
import java.util.UUID;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventDetailsDTO(
    UUID id,
    String title,
    String description,
    LocalDateTime date,
    String city,
    String state,
    String imgUrl,
    String eventUrl,
    Boolean remote,
    List<CouponDTO> coupons) {

  @Builder
  public record CouponDTO(
      String code, Integer discount, LocalDateTime valid) {
  }
}
