package com.vsantos.springtechevents.domain.event;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record EventResponseDTO(UUID id, String title, String description, OffsetDateTime date, String city, String uf,
                Boolean remote, String eventUrl, String imgUrl) {
}