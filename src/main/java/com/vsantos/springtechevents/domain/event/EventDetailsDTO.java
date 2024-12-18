package com.vsantos.springtechevents.domain.event;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record EventDetailsDTO(
    UUID id,
    @Schema(example = "Spring Boot") String title,
    @Schema(example = "A short event description") String description,
    @Schema(example = "2024-12-18T22:26:24.649Z") OffsetDateTime date,
    @Schema(example = "Recife") String city,
    @Schema(example = "PE") String state,
    @Schema(example = "https://th.bing.com/th/id/OIP.2vUTawLyzalDoTv7zF6JTQHaEo?w=2880&h=1800&rs=1&pid=ImgDetMain") String imgUrl,
    @Schema(example = "https://javac.com.br") String eventUrl,
    @Schema(example = "true") Boolean remote,
    List<CouponDTO> coupons) {

  @Builder
  public record CouponDTO(
      @Schema(example = "XLR8") String code,
      @Schema(example = "1000") Integer discount,
      @Schema(example = "2024-12-18T22:26:24.649Z") OffsetDateTime valid) {
  }
}
