package com.vsantos.springtechevents.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record EventResponseDTO(UUID id, String title, String description, LocalDateTime date, String city, String uf,
        Boolean remote, String eventUrl, String imgUrl) {
}